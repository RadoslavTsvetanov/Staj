package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateUsernameException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CredentialsRepo credentialsRepo;

    @Mock
    private PreferencesRepo preferencesRepo;

    @Mock
    private PlanService planService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");
        user.setId(1L);
    }

    @Test
    public void testCreateUser() {
        when(userRepo.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepo.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void testCreateUserWithDuplicateUsername() {
        when(userRepo.existsByUsername(user.getUsername())).thenReturn(true);

        DuplicateUsernameException thrown = assertThrows(
            DuplicateUsernameException.class,
            () -> userService.createUser(user),
            "Expected createUser() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("already exists"));
    }

    @Test
    public void testAddCredentialsAndPreferences() {
        Credentials credentials = new Credentials();
        Preferences preferences = new Preferences();

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(credentialsRepo.save(credentials)).thenReturn(credentials);
        when(preferencesRepo.save(preferences)).thenReturn(preferences);
        when(userRepo.save(user)).thenReturn(user);

        User updatedUser = userService.addCredentialsAndPreferences(user.getId(), credentials, preferences);
        assertEquals(credentials, updatedUser.getCredentials());
        assertEquals(preferences, updatedUser.getPreferences());
    }

    @Test
    public void testDeleteUserAndRemoveFromPlans() {
        when(planService.findPlansByUsername("testuser")).thenReturn(List.of());
        doNothing().when(userRepo).deleteByUsername("testuser");

        userService.deleteUserAndRemoveFromPlans("testuser");

        verify(userRepo, times(1)).deleteByUsername("testuser");
    }

    @Test
    public void testFindByUsername() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.findByUsername("testuser").orElse(null);
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }
}
