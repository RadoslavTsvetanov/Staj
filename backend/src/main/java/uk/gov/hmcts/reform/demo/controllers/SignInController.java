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
@RequestMapping("/api/auth")
public class SignInController {

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthService auth;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        // Log incoming request
        System.out.println("Attempting sign in with email: " + email);

        Credentials credentials = credentialsRepo.findByEmail(email);

        if (credentials == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // Log retrieved credentials
        System.out.println("Retrieved credentials with email: " + credentials.getEmail());

        if (!credentials.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userRepo.findByCredentialsId(credentials.getId());

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String token = auth.issueToken(user.getUsername());
        return ResponseEntity.ok("Sign-in successful. Welcome, " + user.getUsername() + "! Your token: " + token);
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkToken(@RequestBody String token) {
        if (auth.checkIfTokenExists(token)) {
            return ResponseEntity.ok("Token is valid");
        }
        return ResponseEntity.status(401).body("Invalid token");
    }

    public static class SignInRequest {
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
}
