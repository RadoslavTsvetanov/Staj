package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.services.CredentialsService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CredentialsControllerTest {

    @InjectMocks
    private CredentialsController credentialsController;

    @Mock
    private CredentialsService credentialsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCredentialsByEmail_Success_ReturnsCredentials() {
        String email = "test@example.com";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);

        when(credentialsService.findByEmail(email)).thenReturn(credentials);

        ResponseEntity<Credentials> response = credentialsController.getCredentialsByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(credentials, response.getBody());
        verify(credentialsService, times(1)).findByEmail(email);
    }

    @Test
    void getCredentialsByEmail_NotFound_ReturnsNotFound() {
        String email = "test@example.com";

        when(credentialsService.findByEmail(email)).thenReturn(null);

        ResponseEntity<Credentials> response = credentialsController.getCredentialsByEmail(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(credentialsService, times(1)).findByEmail(email);
    }

    @Test
    void createCredentials_Success_ReturnsCreatedCredentials() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");

        when(credentialsService.save(any(Credentials.class))).thenReturn(credentials);

        ResponseEntity<Credentials> response = credentialsController.createCredentials(credentials);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(credentials, response.getBody());
        verify(credentialsService, times(1)).save(any(Credentials.class));
    }

    @Test
    void createCredentials_DuplicateEmail_ReturnsConflict() {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");

        when(credentialsService.save(any(Credentials.class)))
            .thenThrow(new DuplicateEmailException("Email already exists"));

        ResponseEntity<Credentials> response = credentialsController.createCredentials(credentials);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(credentialsService, times(1)).save(any(Credentials.class));
    }

    @Test
    void getAllCredentials_Success_ReturnsAllCredentials() {
        Credentials credentials1 = new Credentials();
        credentials1.setEmail("test1@example.com");

        Credentials credentials2 = new Credentials();
        credentials2.setEmail("test2@example.com");

        List<Credentials> credentialsList = Arrays.asList(credentials1, credentials2);

        when(credentialsService.findAll()).thenReturn(credentialsList);

        ResponseEntity<List<Credentials>> response = credentialsController.getAllCredentials();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(credentialsList, response.getBody());
        verify(credentialsService, times(1)).findAll();
    }
}
