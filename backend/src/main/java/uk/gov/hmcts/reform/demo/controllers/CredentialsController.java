package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.services.CredentialsService;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Credentials> createCredentials(@RequestBody Credentials credentials) {
        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        Credentials savedCredentials = credentialsService.save(credentials);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCredentials);
    }

    @GetMapping
    public ResponseEntity<List<Credentials>> getAllCredentials() {
        List<Credentials> credentialsList = credentialsService.findAll();
        return ResponseEntity.ok(credentialsList);
    }
}

