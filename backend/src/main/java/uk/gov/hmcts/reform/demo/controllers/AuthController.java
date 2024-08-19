package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private UserRepo userRepo;

    // Login Request
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Credentials credentials = credentialsRepo.findByEmail(email);

        if (credentials == null || !credentials.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userRepo.findByCredentialsId(credentials.getId());
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String token = authService.issueToken(user.getUsername());
        return ResponseEntity.ok("Sign-in successful. Token: " + token);
    }

    // Check Token endpoint
    @PostMapping("/check")
    public ResponseEntity<?> checkToken(@RequestBody String token) {
        if (authService.checkIfTokenExists(token)) {
            return ResponseEntity.ok("Token is valid.");
        }
        return ResponseEntity.status(401).body("Invalid token");
    }
}
