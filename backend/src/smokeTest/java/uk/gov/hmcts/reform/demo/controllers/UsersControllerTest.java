package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateUsernameException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UsersControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
            new User("user1", "user1@example.com"),
            new User("user2", "user2@example.com")
        );
        when(userService.findAll()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = usersController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findAll();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        User mockUser = new User("newUser", "newUser@example.com");
        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = usersController.createUser(mockUser);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUser_ShouldReturnConflictOnDuplicateUsername() {
        // Arrange
        User mockUser = new User("newUser", "newUser@example.com");
        when(userService.createUser(any(User.class))).thenThrow(DuplicateUsernameException.class);

        // Act
        ResponseEntity<User> response = usersController.createUser(mockUser);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void addCredentialsToUser_ShouldReturnUpdatedUser() {
        // Arrange
        User mockUser = new User("existingUser", "existingUser@example.com");
        Credentials mockCredentials = new Credentials("user@example.com", "password123");

        // Mock the service method to return the user
        when(userService.addCredentialsAndPreferences(anyLong(), any(Credentials.class), isNull()))
            .thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = usersController.addCredentialsToUser(1L, mockCredentials);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).addCredentialsAndPreferences(anyLong(), any(Credentials.class), isNull());
    }

    // eventualno iprafi tova
//    @Test
//    void addCredentialsToUser_ShouldReturnConflictOnDuplicateEmail() {
//        // Arrange
//        Credentials mockCredentials = new Credentials("duplicate@example.com", "password123");
//
//        // Mock the service to throw the DuplicateEmailException
//        when(userService.addCredentialsAndPreferences(anyLong(), any(Credentials.class), any(Preferences.class)))
//            .thenThrow(new DuplicateEmailException("Duplicate email"));
//
//        // Act
//        ResponseEntity<User> response = usersController.addCredentialsToUser(1L, mockCredentials);
//
//        // Assert
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//        assertNull(response.getBody());
//        verify(userService, times(1)).addCredentialsAndPreferences(anyLong(), any(Credentials.class), isNull());
//    }

    @Test
    void addPreferencesToUser_ShouldReturnUpdatedUser() {
        // Arrange
        User mockUser = new User("existingUser", "existingUser@example.com");
        Preferences mockPreferences = new Preferences();
        when(userService.addCredentialsAndPreferences(anyLong(), isNull(), any(Preferences.class))).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = usersController.addPreferencesToUser(1L, mockPreferences);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).addCredentialsAndPreferences(anyLong(), isNull(), any(Preferences.class));
    }

    @Test
    void searchUsers_ShouldReturnListOfMatchingUsers() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
            new User("user1", "user1@example.com"),
            new User("user2", "user2@example.com")
        );
        when(userService.searchByUsername(anyString())).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = usersController.searchUsers("user");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).searchByUsername("user");
    }

    @Test
    void deleteUser_ShouldReturnOkOnSuccessfulDeletion() {
        // Arrange
        String token = "Bearer validToken";
        String username = "existingUser";
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(username);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        // Act
        ResponseEntity<String> response = usersController.deleteUser(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully.", response.getBody());
        verify(userService, times(1)).deleteUserAndRemoveFromPlans(username);
    }

    @Test
    void deleteUser_ShouldReturnUnauthorizedWhenTokenInvalid() {
        // Act
        ResponseEntity<String> response = usersController.deleteUser(null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authorization token is missing or invalid.", response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNotFoundWhenUserDoesNotExist() {
        // Arrange
        String token = "Bearer validToken";
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("nonExistingUser");
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = usersController.deleteUser(token);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(0)).deleteUserAndRemoveFromPlans(anyString());
    }
}
