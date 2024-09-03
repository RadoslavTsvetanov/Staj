package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.CustomInterestRequest;
import uk.gov.hmcts.reform.demo.services.OpenAIService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InterestControllerTest {

    @Mock
    private OpenAIService openAIService;

    @InjectMocks
    private InterestController interestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessCustomInterest_ValidRequest() {
        String customInterest = "Books";
        List<String> expectedResponse = List.of("Fiction", "Non-fiction");
        when(openAIService.processCustomInterest(anyString())).thenReturn(expectedResponse);

        CustomInterestRequest request = new CustomInterestRequest();
        request.setCustomInterest(customInterest);

        ResponseEntity<List<String>> response = interestController.processCustomInterest(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(openAIService, times(1)).processCustomInterest(customInterest);
    }

    @Test
    void testProcessCustomInterest_InvalidRequest_EmptyCustomInterest() {
        CustomInterestRequest request = new CustomInterestRequest();
        request.setCustomInterest("   ");

        ResponseEntity<List<String>> response = interestController.processCustomInterest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(List.of("Custom interest must not be empty."), response.getBody());
        verify(openAIService, times(0)).processCustomInterest(anyString());
    }

    @Test
    void testProcessCustomInterest_ServiceThrowsException() {
        String customInterest = "Libraries";
        when(openAIService.processCustomInterest(anyString())).thenThrow(new RuntimeException("Service Error"));

        CustomInterestRequest request = new CustomInterestRequest();
        request.setCustomInterest(customInterest);

        ResponseEntity<List<String>> response = interestController.processCustomInterest(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(List.of("An error occurred while processing the request: Service Error"), response.getBody());
        verify(openAIService, times(1)).processCustomInterest(customInterest);
    }
}

