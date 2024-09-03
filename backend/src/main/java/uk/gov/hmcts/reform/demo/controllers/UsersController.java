package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateEmailException;
import uk.gov.hmcts.reform.demo.exceptions.DuplicateUsernameException;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (DuplicateUsernameException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/{userId}/credentials")
    public ResponseEntity<User> addCredentialsToUser(@PathVariable Long userId, @Valid @RequestBody Credentials credentials) {
        try {
            User updatedUser = userService.addCredentialsAndPreferences(userId, credentials, null);
            return ResponseEntity.ok(updatedUser);
        } catch (DuplicateEmailException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/{userId}/preferences")
    public ResponseEntity<User> addPreferencesToUser(@PathVariable Long userId, @Valid @RequestBody Preferences preferences) {
        User updatedUser = userService.addCredentialsAndPreferences(userId, null, preferences);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userService.searchByUsername(username);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/profile/delete")
    public ResponseEntity<String> deleteUser(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing or invalid.");
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }

        Optional<User> userToDeleteOptional = userService.findByUsername(currentUsername);
        if (userToDeleteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        userService.deleteUserAndRemoveFromPlans(currentUsername);
        return ResponseEntity.ok("User deleted successfully.");
    }

}
