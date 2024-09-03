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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.services.CredentialsService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(CredentialsController.class)
public class CredentialsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private CredentialsController credentialsController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCredentialsByEmailSuccess() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("password123");

        when(credentialsService.findByEmail(anyString())).thenReturn(credentials);

        mockMvc.perform(MockMvcRequestBuilders.get("/credentials/email/{email}", "user@example.com")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password123"));

        verify(credentialsService, times(1)).findByEmail(anyString());
    }

    @Test
    public void testGetCredentialsByEmailNotFound() throws Exception {
        when(credentialsService.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/credentials/email/{email}", "nonexistent@example.com")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(credentialsService, times(1)).findByEmail(anyString());
    }

    @Test
    public void testCreateCredentialsSuccess() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("newuser@example.com");
        credentials.setPassword("password123");

        when(credentialsService.save(any(Credentials.class))).thenReturn(credentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newuser@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password123"));

        verify(credentialsService, times(1)).save(any(Credentials.class));
    }

    @Test
    public void testCreateCredentialsConflict() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("existinguser@example.com");
        credentials.setPassword("password123");

        when(credentialsService.save(any(Credentials.class)))
            .thenThrow(new DuplicateEmailException("Email existinguser@example.com already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
            .andExpect(MockMvcResultMatchers.status().isConflict());

        verify(credentialsService, times(1)).save(any(Credentials.class));
    }

    @Test
    public void testGetAllCredentials() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("user1@example.com");
        credentials.setPassword("password123");

        List<Credentials> credentialsList = Collections.singletonList(credentials);

        when(credentialsService.findAll()).thenReturn(credentialsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/credentials")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("user1@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value("password123"));

        verify(credentialsService, times(1)).findAll();
    }
}
