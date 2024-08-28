package uk.gov.hmcts.reform.demo.services;

import uk.gov.hmcts.reform.demo.utils.ApiTypes;

import org.springframework.stereotype.Service;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OpenAIService {

    private static final String OPENAI_API_KEY =
        "###";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient();

    private final ConcurrentMap<String, List<String>> cache = new ConcurrentHashMap<>();

    private static final String[] predefinedInterests = {
        "Food", "Art", "Sport", "Books", "Education", "Entertainment",
        "History", "Hiking", "Movies", "Theater", "Animals", "Shopping",
        "Relax", "Religion", "Flora"
    };

    public List<String> processCustomInterest(String customInterest) {
        String normalizedInterest = customInterest.trim().toLowerCase();

        if (cache.containsKey(normalizedInterest)) {
            return cache.get(normalizedInterest);
        }

        String matchedInterests = getMatchedInterests(customInterest, predefinedInterests);

        if (matchedInterests != null && !matchedInterests.isEmpty()) {
            String cleanedInterests = matchedInterests
                .replaceAll("^-\\s*", "")
                .replaceAll("\\s*-\\s*", ", ")
                .replaceAll("\\s*,\\s*,\\s*", ", ")
                .trim();

            List<String> specificTypes = getSpecificTypesForCustomInterest(customInterest, cleanedInterests);
            specificTypes = formatSpecificTypes(specificTypes);

            List<String> result = !specificTypes.isEmpty() ? specificTypes : List.of("No specific types found.");

            cache.put(normalizedInterest, result);

            return result;
        } else {
            List<String> noMatch = List.of("No matched interests found.");
            cache.put(normalizedInterest, noMatch);
            return noMatch;
        }
    }

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
        json.addProperty("model", "gpt-4");

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
            .append(". Please identify the most relevant types associated with these interests. Only provide specific types, not the interest names themselves. The specific types should be chosen from the following list: ");

        List<String> matchedInterestsList = Arrays.asList(matchedInterests.split("\\s*,\\s*"));
        int limit = Math.min(matchedInterestsList.size(), 2);

        List<String> availableTypes = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String interest = matchedInterestsList.get(i).trim();
            List<String> typesForInterest = ApiTypes.getTypesForInterest(interest);
            if (typesForInterest != null) {
                availableTypes.addAll(typesForInterest);
            }
        }

        if (!availableTypes.isEmpty()) {
            for (String type : availableTypes) {
                promptBuilder.append(type).append(", ");
            }
            promptBuilder.setLength(promptBuilder.length() - 2); // Remove trailing comma and space
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

    private String executeRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
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
