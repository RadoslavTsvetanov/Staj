package uk.gov.hmcts.reform.demo.services;

import uk.gov.hmcts.reform.demo.utils.ApiTypes;

import org.springframework.stereotype.Service;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OpenAIService {

    private static final String OPENAI_API_KEY =
        "###";

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

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
            .url(OPENAI_API_URL)
            .post(body)
            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

        return executeRequest(request);
    }

    public List<String> getSpecificTypesForCustomInterest(String customInterest, String matchedInterests) {
        StringBuilder promptBuilder = new StringBuilder("The user has a specific interest in '")
            .append(customInterest)
            .append("'. Given the following matched interests: ")
            .append(matchedInterests)
            .append(". Please list the relevant types based on the following types: ");

        List<String> matchedInterestsList = Arrays.asList(matchedInterests.split("\\s*,\\s*"));
        int limit = Math.min(matchedInterestsList.size(), 2);

        for (int i = 0; i < limit; i++) {
            String interest = matchedInterestsList.get(i).trim();
            List<String> typesForInterest = ApiTypes.getTypesForInterest(interest);
            if (typesForInterest != null) {
                for (String type : typesForInterest) {
                    promptBuilder.append(type).append(", ");
                }
            }
        }

        if (promptBuilder.length() > 0) {
            promptBuilder.setLength(promptBuilder.length() - 2);
        }

        promptBuilder.append(". Provide only the most relevant types for the custom interest in the format: \"type1\", \"type2\", \"type3\". The relevant types need to be written exactly like the types in the matched interests.");

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
            .url(OPENAI_API_URL)
            .post(body)
            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

        String response = executeRequest(request);
        return parseTypesFromResponse(response);
    }

    public List<String> processCustomInterest(String customInterest) {
        String[] predefinedInterests = {
            "Food", "Art", "Sport", "Books", "Education", "Entertainment",
            "History", "Hiking", "Movies", "Theater", "Animals", "Shopping",
            "Relax", "Religion", "Flora"
        };

        String matchedInterests = getMatchedInterests(customInterest, predefinedInterests);

        if (matchedInterests != null && !matchedInterests.isEmpty()) {
            String cleanedInterests = matchedInterests
                .replaceAll("^-\\s*", "")
                .replaceAll("\\s*-\\s*", ", ")
                .replaceAll("\\s*,\\s*,\\s*", ", ")
                .trim();

            List<String> specificTypes = getSpecificTypesForCustomInterest(customInterest, cleanedInterests);
            specificTypes = formatSpecificTypes(specificTypes);

            return !specificTypes.isEmpty() ? specificTypes : Arrays.asList("No specific types found.");
        } else {
            return Arrays.asList("No matched interests found.");
        }
    }

    private String executeRequest(Request request) {
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
            return "Error occurred: " + e.getMessage();
        }
    }

    private List<String> parseTypesFromResponse(String response) {
        List<String> typesList = new ArrayList<>();
        if (response != null && !response.isEmpty()) {
            response = response.replaceAll("\"", "");
            typesList = Arrays.asList(response.split("\\s*,\\s*"));
        }
        return typesList;
    }

    private List<String> formatSpecificTypes(List<String> specificTypes) {
        List<String> formattedTypes = new ArrayList<>();
        for (String type : specificTypes) {
            if (type.contains(" ")) {
                type = type.replace(" ", "_");
            }
            if (Character.isUpperCase(type.charAt(0))) {
                type = Character.toLowerCase(type.charAt(0)) + type.substring(1);
            }
            formattedTypes.add(type);
        }
        return formattedTypes;
    }
}
