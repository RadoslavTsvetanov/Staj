package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.controllers.SignInController.SignInRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignInControllerTest {

    @Mock
    private CredentialsRepo credentialsRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthService authService;

    @InjectMocks
    private SignInController signInController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn_Success() {
        String email = "test@example.com";
        String password = "password";
        String username = "testuser";

        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setCredentials(credentials);

        when(credentialsRepo.findByEmail(email)).thenReturn(credentials);
        when(userRepo.findByCredentialsId(credentials.getId())).thenReturn(user);
        when(authService.issueToken(username)).thenReturn("token123");

        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResponseEntity<?> response = signInController.signIn(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token123", response.getBody());
    }

    @Test
    void testSignIn_InvalidEmailOrPassword() {
        String email = "test@example.com";
        String password = "wrongpassword";

        when(credentialsRepo.findByEmail(email)).thenReturn(null);

        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResponseEntity<?> response = signInController.signIn(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid email or password", response.getBody());
    }

    @Test
    void testSignIn_UserNotFound() {
        String email = "test@example.com";
        String password = "password";

        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);

        when(credentialsRepo.findByEmail(email)).thenReturn(credentials);
        when(userRepo.findByCredentialsId(credentials.getId())).thenReturn(null);

        SignInRequest request = new SignInRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResponseEntity<?> response = signInController.signIn(request);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testCheckToken_ValidToken() {
        String token = "validToken";
        when(authService.checkIfTokenExists(token)).thenReturn(true);

        ResponseEntity<?> response = signInController.checkToken(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token is valid", response.getBody());
    }

    @Test
    void testCheckToken_InvalidToken() {
        String token = "invalidToken";
        when(authService.checkIfTokenExists(token)).thenReturn(false);

        ResponseEntity<?> response = signInController.checkToken(token);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid token", response.getBody());
    }
}
