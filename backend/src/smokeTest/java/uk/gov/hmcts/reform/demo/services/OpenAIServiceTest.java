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
//import static org.mockito.ArgumentMatchers.anyString;
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
//        String customInterest = "Museums";
//        List<String> cachedResult = List.of("Cached Interest");
//        when(cache.get(anyString())).thenReturn(cachedResult);
//
//        List<String> result = openAIService.processCustomInterest(customInterest);
//
//        assertEquals(cachedResult, result);
//        verify(cache, times(1)).get("museums");
//    }
//
//    @Test
//    void testProcessCustomInterest_noMatchFound() {
//
//        String customInterest = "Theater";
//        when(cache.get(anyString())).thenReturn(null);
//        when(openAIService.getMatchedInterests(anyString(), any())).thenReturn(null);
//
//        List<String> result = openAIService.processCustomInterest(customInterest);
//
//        assertEquals(List.of("No matched interests found."), result);
//        verify(cache, times(1)).put("theater", List.of("No matched interests found."));
//    }
//
//    @Test
//    void testGetMatchedInterests_success() throws IOException {
//        String customInterest = "Museums";
//        String expectedResponse = "Art, History, Education, Entertainment";
//        mockOkHttpResponse(200, createMockOpenAIResponse(expectedResponse));
//
//        String matchedInterests = openAIService.getMatchedInterests(customInterest, new String[]{"Art", "History", "Education", "Entertainment"});
//
//        assertEquals(expectedResponse, matchedInterests);
//    }
//
//    @Test
//    void testGetSpecificTypesForCustomInterest_success() throws IOException {
//        String customInterest = "Museums";
//        String matchedInterests = "Art, History";
//        String expectedResponse = "\"museum\"";
//        mockOkHttpResponse(200, createMockOpenAIResponse(expectedResponse));
//
//        List<String> specificTypes = openAIService.getSpecificTypesForCustomInterest(customInterest, matchedInterests);
//
//        assertEquals(List.of("museum"), specificTypes);
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
