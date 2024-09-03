//package uk.gov.hmcts.reform.demo.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import okhttp3.*;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class OpenAIServiceTest {
//
//    @Mock
//    private Cache cache;
//
//    @Mock
//    private OkHttpClient client;
//
//    @InjectMocks
//    private OpenAIService openAIService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testProcessCustomInterest_withCachedResult() {
//        String customInterest = "Libraries";
//        List<String> cachedResult = List.of("Cached Interest");
//
//        when(cache.get(eq("libraries"))).thenReturn(cachedResult);
//
//        List<String> result = openAIService.processCustomInterest(customInterest);
//
//        assertEquals(cachedResult, result);
//        verify(cache, times(1)).get(eq("libraries"));
//    }
//
//    @Test
//    void testProcessCustomInterest_noMatchFound() {
//        String customInterest = "Theater";
//        String normalizedInterest = customInterest.trim().toLowerCase();
//
//        // Mock cache behavior
//        when(cache.get(normalizedInterest)).thenReturn(null);
//
//        // Since OpenAIService is a real instance, we need to ensure getMatchedInterests is called correctly
//        // Here, use any() correctly in stubbing
//        when(openAIService.getMatchedInterests(eq(customInterest), any(String[].class))).thenReturn("No matched interests found.");
//
//        // Execute the method under test
//        List<String> result = openAIService.processCustomInterest(customInterest);
//
//        assertEquals(List.of("No matched interests found."), result);
//
//        verify(cache).put(eq(normalizedInterest), eq(List.of("No matched interests found.")));
//    }
//
//
//    @Test
//    void testGetMatchedInterests_success() throws IOException {
//        String customInterest = "Libraries";
//        String expectedResponse = "1. Books\n2. Education\n3. History";
//        mockOkHttpResponse(200, createMockOpenAIResponse(expectedResponse));
//
//        String matchedInterests = openAIService.getMatchedInterests(customInterest, new String[]{"Books", "Education", "History"});
//
//        assertEquals(expectedResponse, matchedInterests);
//    }
//
//    @Test
//    void testGetSpecificTypesForCustomInterest_success() throws IOException {
//        String customInterest = "Libraries";
//        String matchedInterests = "Books, Education";
//        String expectedResponse = "\"library\", \"university\""; // Ensure this matches the response format
//
//        mockOkHttpResponse(200, createMockOpenAIResponse(expectedResponse));
//
//        List<String> specificTypes = openAIService.getSpecificTypesForCustomInterest(customInterest, matchedInterests);
//
//        // Adjust expected result based on actual response
//        assertEquals(List.of("library", "university"), specificTypes);
//    }
//
//    private void mockOkHttpResponse(int statusCode, String responseBody) throws IOException {
//        ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), responseBody);
//        Response response = new Response.Builder()
//            .request(new Request.Builder().url("https://api.openai.com/v1/chat/completions").build())
//            .protocol(Protocol.HTTP_1_1)
//            .code(statusCode)
//            .message("")
//            .body(body)
//            .build();
//        Call call = mock(Call.class);
//        when(call.execute()).thenReturn(response);
//        when(client.newCall(any(Request.class))).thenReturn(call);
//    }
//
//    private String createMockOpenAIResponse(String content) {
//        return "{ \"choices\": [{ \"message\": { \"content\": \"" + content + "\" } }] }";
//    }
//}
