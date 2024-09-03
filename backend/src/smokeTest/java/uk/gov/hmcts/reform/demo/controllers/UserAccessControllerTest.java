package uk.gov.hmcts.reform.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.demo.models.*;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.services.EntityToDtoMapper;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;
import uk.gov.hmcts.reform.demo.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAccessController.class)
public class UserAccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PlanService planService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthService authService;

    @Mock
    private PreferencesRepo preferencesRepo;

    @Mock
    private CredentialsRepo credentialsRepo;

    @InjectMocks
    private UserAccessController userAccessController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserFriendsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/friends")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserFriends() throws Exception {
        String token = "validtoken";
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        Set<User> friends = new HashSet<>();
        User friend = new User();
        friend.setUsername("friend");
        friends.add(friend);
        user.setFriends(friends);

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/friends")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("friend"));
    }

    @Test
    public void testGetUserPlansUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/plans")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserPlans() throws Exception {
        String token = "validtoken";
        String username = "testuser";
        List<Plan> plans = new ArrayList<>();
        plans.add(new Plan());
        List<PlanDTO> planDTOs = plans.stream()
            .map(EntityToDtoMapper::toPlanDTO)
            .collect(Collectors.toList());

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(planService.findPlansByUsername(username)).thenReturn(plans);

        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/plans")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testAddFriendUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/friends/add")
                            .param("friendUsername", "frienduser")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAddFriend() throws Exception {
        String token = "validtoken";
        String currentUsername = "currentuser";
        String friendUsername = "frienduser";
        User currentUser = new User();
        currentUser.setUsername(currentUsername);
        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);
        when(userService.findByUsername(currentUsername)).thenReturn(Optional.of(currentUser));
        when(userService.findByUsername(friendUsername)).thenReturn(Optional.of(friendUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/friends/add")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .param("friendUsername", friendUsername)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Friend added successfully."));
    }

    @Test
    public void testAddFriendAlreadyFriends() throws Exception {
        String token = "validtoken";
        String currentUsername = "currentuser";
        String friendUsername = "frienduser";
        User currentUser = new User();
        currentUser.setUsername(currentUsername);
        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        currentUser.getFriends().add(friendUser);

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(currentUsername);
        when(userService.findByUsername(currentUsername)).thenReturn(Optional.of(currentUser));
        when(userService.findByUsername(friendUsername)).thenReturn(Optional.of(friendUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/friends/add")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .param("friendUsername", friendUsername)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Already friends."));
    }

    @Test
    public void testGetUserProfileUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/profile")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserProfile() throws Exception {
        String token = "validtoken";
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setProfilePicture("profile.jpg");

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/user-access/profile")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(username))
            .andExpect(jsonPath("$.profilePicture").value("/uploads/profile_pictures/profile.jpg"));
    }

    @Test
    public void testUpdateUserProfileUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/profile/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new User())))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        String token = "validtoken";
        String username = "testuser";
        User updatedUser = new User();
        updatedUser.setUsername(username);
        updatedUser.setCredentials(new Credentials());
        updatedUser.getCredentials().setEmail("newemail@example.com");

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setCredentials(new Credentials());
        existingUser.getCredentials().setEmail("oldemail@example.com");

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(userService.checkIfEmailExists("newemail@example.com")).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(updatedUser);
        when(authService.issueToken(anyString())).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/profile/update")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + token))
            .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    public void testUploadProfilePictureUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user-access/profile/upload-picture")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUploadProfilePicture() throws Exception {
        String token = "validtoken";
        String username = "testuser";
        User existingUser = new User();
        existingUser.setUsername(username);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn("filecontent".getBytes());
        when(file.getOriginalFilename()).thenReturn("profile.jpg");

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(Utils.uploadFile(any(byte[].class), anyString(), anyString())).thenReturn("File uploaded");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/user-access/profile/upload-picture")
                            .file("file", "filecontent".getBytes())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().string("Profile picture uploaded successfully! Server response: File uploaded"));
    }
}
