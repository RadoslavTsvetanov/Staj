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
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(RegistrationContoller.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CredentialsRepo credentialsRepo;

    @Mock
    private PreferencesRepo preferencesRepo;

    @InjectMocks
    private RegistrationContoller registrationController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterBasicInfoUsernameExists() throws Exception {
        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/basic")
                            .param("username", "existinguser")
                            .param("password", "password123")
                            .param("email", "user@example.com")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Username already exists"));

        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialsRepo, never()).existsByEmail(anyString());
    }

    @Test
    public void testRegisterBasicInfoEmailExists() throws Exception {
        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(credentialsRepo.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/basic")
                            .param("username", "newuser")
                            .param("password", "password123")
                            .param("email", "existing@example.com")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Email already exists"));

        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialsRepo, times(1)).existsByEmail(anyString());
    }

    @Test
    public void testRegisterBasicInfoSuccess() throws Exception {
        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(credentialsRepo.existsByEmail(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/basic")
                            .param("username", "newuser")
                            .param("password", "password123")
                            .param("email", "new@example.com")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Basic information registered. Proceed to complete registration"));

        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialsRepo, times(1)).existsByEmail(anyString());
        verify(credentialsRepo, times(1)).save(any(Credentials.class));
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void testCompleteRegistrationUserNotFound() throws Exception {
        when(userRepo.findByUsername(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/complete")
                            .param("username", "nonexistentuser")
                            .param("dateOfBirth", "2000-01-01")
                            .param("name", "Test User")
                            .param("interests", "reading", "coding")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("User not found"));

        verify(userRepo, times(1)).findByUsername(anyString());
        verify(preferencesRepo, never()).save(any(Preferences.class));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void testCompleteRegistrationSuccess() throws Exception {
        User user = new User();
        user.setUsername("newuser");

        when(userRepo.findByUsername(anyString())).thenReturn(user);
        when(preferencesRepo.save(any(Preferences.class))).thenReturn(new Preferences());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/complete")
                            .param("username", "newuser")
                            .param("dateOfBirth", "2000-01-01")
                            .param("name", "Test User")
                            .param("interests", "reading", "coding")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Registration complete"));

        verify(userRepo, times(1)).findByUsername(anyString());
        verify(preferencesRepo, times(1)).save(any(Preferences.class));
        verify(userRepo, times(1)).save(any(User.class));
    }
}
