package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import okhttp3.*;
import org.mockito.Spy;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OpenAIServiceTest {

    @Mock
    private Cache cache;

    @Mock
    private OkHttpClient client;

    @Spy
    @InjectMocks
    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessCustomInterest_withCachedResult() {
        String customInterest = "Libraries";
        List<String> cachedResult = List.of("Cached Interest");

        when(cache.get(eq("libraries"))).thenReturn(cachedResult);

        List<String> result = openAIService.processCustomInterest(customInterest);

        assertEquals(cachedResult, result);
        verify(cache, times(1)).get(eq("libraries"));
    }

    @Test
    void testProcessCustomInterest_noMatchFound() {
        String customInterest = "Zoo";
        String normalizedInterest = customInterest.trim().toLowerCase();

        when(cache.get(normalizedInterest)).thenReturn(null);

        doReturn("as_no_matched_interests_were_found, the_most_relevant_types_associated_with_the_interest_in_'Zoo'_would_be:\n\n- Zoo").when(openAIService).getMatchedInterests(eq(customInterest), any(String[].class));

        List<String> result = openAIService.processCustomInterest(customInterest);

        assertEquals(List.of("zoo"), result);
        verify(cache).put(eq(normalizedInterest), eq(List.of("zoo")));
    }

    @Test
    void testGetMatchedInterests_success() throws IOException {
        String customInterest = "Libraries";
        String mockResponse = "1. Books\n2. Education\n3. History";
        mockOkHttpResponse(200, createMockOpenAIResponse(mockResponse));

        String matchedInterests = openAIService.getMatchedInterests(customInterest, new String[]{"Books", "Education", "History"});

        assertEquals("1. Books\n2. Education\n3. History", matchedInterests);
    }

    @Test
    void testGetSpecificTypesForCustomInterest_success() throws IOException {
        String customInterest = "Libraries";
        String matchedInterests = "Books, Education";
        String mockResponse = "\"library\", \"university\""; // Adjusted mock response to include multiple types

        mockOkHttpResponse(200, createMockOpenAIResponse(mockResponse));

        List<String> specificTypes = openAIService.getSpecificTypesForCustomInterest(customInterest, matchedInterests);

        assertEquals(List.of("library", "university"), specificTypes);
    }

    private void mockOkHttpResponse(int statusCode, String responseBody) throws IOException {
        ResponseBody body = ResponseBody.create(responseBody, MediaType.parse("application/json"));
        Response response = new Response.Builder()
            .request(new Request.Builder().url("https://api.openai.com/v1/chat/completions").build())
            .protocol(Protocol.HTTP_1_1)
            .code(statusCode)
            .message("Mocked")
            .body(body)
            .build();
        Call call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(client.newCall(any(Request.class))).thenReturn(call);
    }

    private String createMockOpenAIResponse(String content) {
        return "{ \"choices\": [{ \"message\": { \"content\": \"" + content.replace("\n", "\\n").replace("\"", "\\\"") + "\" } }] }";
    }
}
