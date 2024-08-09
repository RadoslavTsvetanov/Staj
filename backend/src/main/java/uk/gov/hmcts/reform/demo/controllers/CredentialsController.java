package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.services.CredentialsService;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/email/{email}")
    public ResponseEntity<Credentials> getCredentialsByEmail(@PathVariable String email) {
        Credentials credentials = credentialsService.findByEmail(email);
        if (credentials == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credentials);
    }
}

