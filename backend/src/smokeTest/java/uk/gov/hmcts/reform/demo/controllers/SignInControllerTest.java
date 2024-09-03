package uk.gov.hmcts.reform.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignInController.class)
public class SignInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CredentialsRepo credentialsRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthService authService;

    @InjectMocks
    private SignInController signInController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignInInvalidCredentials() throws Exception {
        SignInController.SignInRequest request = new SignInController.SignInRequest();
        request.setEmail("user@example.com");
        request.setPassword("wrongpassword");

        when(credentialsRepo.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid email or password"));
    }

    @Test
    public void testSignInUserNotFound() throws Exception {
        SignInController.SignInRequest request = new SignInController.SignInRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        Credentials credentials = new Credentials();
        credentials.setId(1L);
        credentials.setPassword("password");

        when(credentialsRepo.findByEmail(anyString())).thenReturn(credentials);
        when(userRepo.findByCredentialsId(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("User not found"));
    }

    @Test
    public void testSignInSuccessful() throws Exception {
        SignInController.SignInRequest request = new SignInController.SignInRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        Credentials credentials = new Credentials();
        credentials.setId(1L);
        credentials.setPassword("password");

        User user = new User();
        user.setUsername("testuser");

        when(credentialsRepo.findByEmail(anyString())).thenReturn(credentials);
        when(userRepo.findByCredentialsId(anyLong())).thenReturn(user);
        when(authService.issueToken(anyString())).thenReturn("valid-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("valid-token"));
    }

    @Test
    public void testCheckTokenValid() throws Exception {
        String token = "valid-token";

        when(authService.checkIfTokenExists(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(token)))
            .andExpect(status().isOk())
            .andExpect(content().string("Token is valid"));
    }

    @Test
    public void testCheckTokenInvalid() throws Exception {
        String token = "invalid-token";

        when(authService.checkIfTokenExists(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(token)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid token"));
    }
}
