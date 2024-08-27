package uk.gov.hmcts.reform.demo.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenAIServiceTest {

    private MockWebServer mockWebServer;
    private OpenAIService openAIService;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();

        mockWebServer.start();

        openAIService = new OpenAIService();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testGetSpecificTypesForCustomInterest() throws Exception {
        String mockResponseBody = "{\"choices\": [{\"message\": {\"content\": \"[\\\"type1\\\", \\\"type2\\\"]\"}}]}";
        mockWebServer.enqueue(new MockResponse()
                                  .setBody(mockResponseBody)
                                  .addHeader("Content-Type", "application/json"));

        List<String> types = openAIService.getSpecificTypesForCustomInterest("Art", "visual_Arts");

        assertEquals(2, types.size());
        assertEquals("type1", types.get(0));
        assertEquals("type2", types.get(1));
    }
}

