package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserAccessControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PlanService planService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthService auth;

    @Mock
    private PreferencesRepo preferencesRepo;

    @Mock
    private CredentialsRepo credentialsRepo;

    @InjectMocks
    private UserAccessController userAccessController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserFriends_ValidToken() {
        String token = "valid-token";
        String username = "johnDoe";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        User user = new User();
        user.setUsername(username);
        User friend = new User();
        friend.setUsername("friend1");
        user.setFriends(Set.of(friend));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<List<FriendDTO>> response = userAccessController.getUserFriends("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("friend1", response.getBody().get(0).getUsername());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserFriends_InvalidToken() {
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(null);

        ResponseEntity<List<FriendDTO>> response = userAccessController.getUserFriends("Bearer invalid-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).findByUsername(anyString());
    }

    @Test
    void testGetUserFriends_UserNotFound() {
        String token = "valid-token";
        String username = "johnDoe";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<List<FriendDTO>> response = userAccessController.getUserFriends("Bearer " + token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserPlans_ValidToken() {
        String token = "valid-token";
        String username = "johnDoe";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        Plan plan = new Plan();
        plan.setName("Plan 1");
        when(planService.findPlansByUsername(username)).thenReturn(List.of(plan));

        ResponseEntity<List<PlanDTO>> response = userAccessController.getUserPlans("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Plan 1", response.getBody().get(0).getName());
        verify(planService, times(1)).findPlansByUsername(username);
    }

    @Test
    void testGetUserPlans_InvalidToken() {
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn(null);

        ResponseEntity<List<PlanDTO>> response = userAccessController.getUserPlans("Bearer invalid-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(planService, never()).findPlansByUsername(anyString());
    }

    @Test
    void testAddFriend_Success() {
        String token = "valid-token";
        String currentUsername = "johnDoe";
        String friendUsername = "friend1";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);

        User currentUser = new User();
        currentUser.setUsername(currentUsername);
        when(userService.findByUsername(currentUsername)).thenReturn(Optional.of(currentUser));

        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        when(userService.findByUsername(friendUsername)).thenReturn(Optional.of(friendUser));

        ResponseEntity<?> response = userAccessController.addFriend("Bearer " + token, friendUsername);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Friend added successfully.", response.getBody());
        verify(userService, times(2)).save(any(User.class));
    }

    @Test
    void testAddFriend_AlreadyFriends() {
        String token = "valid-token";
        String currentUsername = "johnDoe";
        String friendUsername = "friend1";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);

        User currentUser = new User();
        currentUser.setUsername(currentUsername);
        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        currentUser.setFriends(Set.of(friendUser));
        when(userService.findByUsername(currentUsername)).thenReturn(Optional.of(currentUser));
        when(userService.findByUsername(friendUsername)).thenReturn(Optional.of(friendUser));

        ResponseEntity<?> response = userAccessController.addFriend("Bearer " + token, friendUsername);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Already friends.", response.getBody());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    void testAddFriend_UserNotFound() {
        String token = "valid-token";
        String currentUsername = "johnDoe";
        String friendUsername = "friend1";
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);

        when(userService.findByUsername(currentUsername)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userAccessController.addFriend("Bearer " + token, friendUsername);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Current user not found.", response.getBody());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserProfile_Success() {
        String token = "valid-token";
        String currentUsername = "johnDoe";
        User updatedUser = new User();
        updatedUser.setUsername(currentUsername);
        Credentials updatedCredentials = new Credentials();
        updatedCredentials.setEmail("newemail@example.com");
        updatedUser.setCredentials(updatedCredentials);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);

        User existingUser = new User();
        existingUser.setUsername(currentUsername);
        existingUser.setCredentials(new Credentials());
        when(userService.findByUsername(currentUsername)).thenReturn(Optional.of(existingUser));
        when(userService.checkIfEmailExists(anyString())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(existingUser);
        when(auth.issueToken(anyString())).thenReturn("new-token");

        ResponseEntity<?> response = userAccessController.updateUserProfile("Bearer " + token, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser, response.getBody());
        verify(userService, times(1)).save(existingUser);
        verify(auth, times(1)).issueToken(currentUsername);
    }

//   TOva kato mergna s na rado trqbva da go orpravq da raboti
//    @Test
//    void testUploadProfilePicture_Success() throws IOException {
//        String token = "valid-token";
//        String username = "johnDoe";
//        MultipartFile file = mock(MultipartFile.class);
//        when(file.getBytes()).thenReturn(new byte[0]);
//        when(file.getOriginalFilename()).thenReturn("profile.jpg");
//
//        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
//
//        User existingUser = new User();
//        existingUser.setUsername(username);
//        when(userService.findByUsername(username)).thenReturn(Optional.of(existingUser));
//        when(userService.save(existingUser)).thenReturn(existingUser);
//
//        ResponseEntity<?> response = userAccessController.uploadProfilePicture("Bearer " + token, file);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Profile picture uploaded successfully! Server response: null", response.getBody());
//        verify(userService, times(1)).save(existingUser);
//    }

    @Test
    void testUploadProfilePicture_UserNotFound() {
        String token = "valid-token";
        String username = "johnDoe";
        MultipartFile file = mock(MultipartFile.class);

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userAccessController.uploadProfilePicture("Bearer " + token, file);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
        verify(userService, never()).save(any(User.class));
    }
}
