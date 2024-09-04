package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @InjectMocks
    private RegistrationContoller registrationController;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CredentialsRepo credentialsRepo;

    @Mock
    private PreferencesRepo preferencesRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBasicInfo_UserExists_ReturnsBadRequest() {
        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        ResponseEntity<String> response = registrationController.registerBasicInfo("testuser", "password", "test@example.com");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username already exists", response.getBody());
    }

    @Test
    void registerBasicInfo_EmailExists_ReturnsBadRequest() {
        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(credentialsRepo.existsByEmail(anyString())).thenReturn(true);

        ResponseEntity<String> response = registrationController.registerBasicInfo("newuser", "password", "test@example.com");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void registerBasicInfo_Success_ReturnsOk() {
        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(credentialsRepo.existsByEmail(anyString())).thenReturn(false);

        ResponseEntity<String> response = registrationController.registerBasicInfo("newuser", "password", "newuser@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Basic information registered. Proceed to complete registration.", response.getBody());

        verify(credentialsRepo, times(1)).save(any(Credentials.class));
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void completeRegistration_UserNotFound_ReturnsBadRequest() {
        when(userRepo.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<String> response = registrationController.completeRegistration("unknownuser", LocalDate.now(), "Name", List.of("Interest"));

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void completeRegistration_Success_ReturnsOk() {
        User existingUser = new User();
        when(userRepo.findByUsername(anyString())).thenReturn(existingUser);

        ResponseEntity<String> response = registrationController.completeRegistration("existinguser", LocalDate.of(2000, 1, 1), "Name", List.of("Interest1", "Interest2"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registration complete", response.getBody());

        verify(preferencesRepo, times(1)).save(any(Preferences.class));
        verify(userRepo, times(1)).save(any(User.class));
    }
}
