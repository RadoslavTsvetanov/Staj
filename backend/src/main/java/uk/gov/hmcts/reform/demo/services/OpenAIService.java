package uk.gov.hmcts.reform.demo.services;

import uk.gov.hmcts.reform.demo.utils.ApiTypes;

import org.springframework.stereotype.Service;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

@Service
public class OpenAIService {

    private static final String OPENAI_API_KEY =
        "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";

    private final OkHttpClient client = new OkHttpClient();

    public String getMatchedInterests(String customInterest, String[] predefinedInterests) {
        StringBuilder promptBuilder = new StringBuilder("The user has entered the custom interest '")
            .append(customInterest)
            .append("'. Please match it with one or more of the following predefined interests: ");

        for(String interest : predefinedInterests) {
            promptBuilder.append(interest).append(", ");
        }
        promptBuilder.setLength(promptBuilder.length() - 2);
        promptBuilder.append(". Provide a list of the most relevant interests starting with the most compatible one.");

        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-3.5-turbo");

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();

        message.addProperty("role", "user");
        message.addProperty("content", promptBuilder.toString());
        messages.add(message);

        json.add("messages", messages);
        json.addProperty("max_tokens", 80);

        RequestBody body = RequestBody.create(
            MediaType.parse("application/json"),
            json.toString()
        );

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                System.err.println("Unexpected code: " + response);
                System.err.println("Response body: " + response.body().string());

                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
            JsonArray choices = jsonResponse.getAsJsonArray("choices");

            if (!choices.isEmpty()) {
                return choices.get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString().trim();
            } else {
                return "No response received.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDestinationsForInterestAndTypes(String customInterest, String matchedInterests) {
        StringBuilder promptBuilder = new StringBuilder("The user has a specific interest in '")
            .append(customInterest)
            .append("'. Given the following matched interests: ");

        Dictionary<String, List<String>> apiTypesDict = ApiTypes.getCustomApiTypesToGoogleApiTypesDict();

        String[] interestsArray = matchedInterests.split("\\s*,\\s*");
        int limit = Math.min(interestsArray.length, 2); // the two most compatible categories

        for (int i = 0; i < limit; i++) {
            String interest = interestsArray[i].trim();
            promptBuilder.append(interest).append(", ");
            List<String> types = apiTypesDict.get(interest);

            if (types != null && !types.isEmpty()) {
                promptBuilder.append(" with types: ").append(String.join(", ", types)).append("; ");
            }
        }

        promptBuilder.append("Please list specific destinations or activities that are related to '")
            .append(customInterest)
            .append("' within these matched interests.");

        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-3.5-turbo");

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();

        message.addProperty("role", "user");
        message.addProperty("content", promptBuilder.toString());
        messages.add(message);

        json.add("messages", messages);
        json.addProperty("max_tokens", 100);

        RequestBody body = RequestBody.create(
            MediaType.parse("application/json"),
            json.toString()
        );

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Unexpected code: " + response);
                System.err.println("Response body: " + response.body().string());

                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
            JsonArray choices = jsonResponse.getAsJsonArray("choices");

            if (!choices.isEmpty()) {
                return choices.get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString().trim();
            } else {
                return "No response received.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void runTest() {
        String[] predefinedInterests =
                {"Food", "Art", "Sport", "Books", "Education", "Entertainment", "History",
                "Hiking", "Movies", "Theater", "Animals", "Shopping", "Relax", "Religion", "Flora"};

        String customInterest = "Piano"; // vzima se ot user-a

        String matchedInterests = getMatchedInterests(customInterest, predefinedInterests);

        if(matchedInterests != null) {
            String cleanedInterests = matchedInterests
                .replaceAll("^-\\s*", "") // Remove leading hyphens and spaces
                .replaceAll("\\s*-\\s*", ", ") // Replace hyphens with commas
                .replaceAll("\\s*,\\s*,\\s*", ", ") // Handle multiple commas
                .trim(); // Trim any leading or trailing spaces

            List<String> interests = Arrays.asList(cleanedInterests.split("\\s*,\\s*"));

            for(int i = 0; i < interests.size(); i++) {
                String interest = interests.get(i).trim();
                if(!interest.isEmpty()) {
                    System.out.println("Interest " + (i + 1) + ": " + interest);
                }
            }
            String destinations = getDestinationsForInterestAndTypes(customInterest, cleanedInterests);
            System.out.println("Suggested Destinations: " + destinations);
        } else {
            System.out.println("Failed to match interests.");
        }
    }
}
