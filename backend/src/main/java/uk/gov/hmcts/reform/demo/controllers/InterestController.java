package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.CustomInterestRequest;
import uk.gov.hmcts.reform.demo.services.OpenAIService;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final OpenAIService openAIService;

    @Autowired
    public InterestController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processCustomInterest(@RequestBody CustomInterestRequest request) {
        String customInterest = request.getCustomInterest();

        if (customInterest == null || customInterest.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Custom interest must not be empty.");
        }

        try {
            String result = openAIService.processCustomInterest(customInterest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the request: " + e.getMessage());
        }
    }
}
