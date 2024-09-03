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
import uk.gov.hmcts.reform.demo.exceptions.DuplicateUsernameException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsersController usersController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("newuser"));
    }

    @Test
    public void testCreateUserDuplicateUsername() throws Exception {
        User user = new User();
        user.setUsername("existinguser");
        when(userService.createUser(any(User.class))).thenThrow(new DuplicateUsernameException("Username already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testAddCredentialsToUser() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("test@example.com");
        User user = new User();
        user.setUsername("user");

        when(userService.addCredentialsAndPreferences(anyLong(), any(Credentials.class), isNull())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"));
    }

    @Test
    public void testAddCredentialsToUserDuplicateEmail() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setEmail("duplicate@example.com");

        when(userService.addCredentialsAndPreferences(anyLong(), any(Credentials.class), isNull())).thenThrow(new DuplicateEmailException("Email already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/credentials")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(credentials)))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testAddPreferencesToUser() throws Exception {
        Preferences preferences = new Preferences();
        User user = new User();
        user.setUsername("user");

        when(userService.addCredentialsAndPreferences(anyLong(), isNull(), any(Preferences.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/preferences")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(preferences)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"));
    }

    @Test
    public void testSearchUsers() throws Exception {
        User user = new User();
        user.setUsername("searchuser");
        List<User> users = List.of(user);

        when(userService.searchByUsername(anyString())).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                            .param("username", "searchuser")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("searchuser"));
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/profile/delete")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.content().string("Authorization token is missing or invalid."));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("nonexistentuser");
        when(userService.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/profile/delete")
                            .header("Authorization", "Bearer validtoken")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().string("User not found."));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("user");
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("user");
        when(userService.findByUsername("user")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/profile/delete")
                            .header("Authorization", "Bearer validtoken")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("User deleted successfully."));
    }
}
