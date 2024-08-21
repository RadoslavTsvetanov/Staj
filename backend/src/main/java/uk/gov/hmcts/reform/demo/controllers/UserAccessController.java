package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.models.Plan;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.services.AuthService;
import uk.gov.hmcts.reform.demo.services.PlanService;
import uk.gov.hmcts.reform.demo.services.UserService;
import uk.gov.hmcts.reform.demo.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-access")
public class UserAccessController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService auth;

    @Autowired
    private PreferencesRepo preferencesRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getUserPlans(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<Plan> plans = planService.findPlansByUsername(username);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Optional<User> userOptional = userService.findByUsername(username);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(
        @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
        @RequestBody User updatedUser) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authorizationHeader.substring(7);
        String currentUsername = jwtUtil.getUsernameFromToken(token);

        if (currentUsername == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> existingUserOpt = userService.findByUsername(currentUsername);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (!existingUser.getCredentials().getEmail().equals(updatedUser.getCredentials().getEmail())) {
                boolean emailExists = userService.checkIfEmailExists(updatedUser.getCredentials().getEmail());
                if (emailExists) {
                    return ResponseEntity.status(409).body("Email is already registered.");
                }
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

            if (updatedUser.getPreferences() != null) {
                Preferences updatedPreferences = updatedUser.getPreferences();
                if (updatedPreferences.getId() == null) {
                    // New preferences, save them
                    preferencesRepo.save(updatedPreferences);
                    existingUser.setPreferences(updatedPreferences);
                } else {
                    // Existing preferences, update them
                    preferencesRepo.save(updatedPreferences);
                    existingUser.setPreferences(updatedPreferences);
                }
            }

            Credentials updatedCredentials = updatedUser.getCredentials();
            if (updatedCredentials.getId() != null) {
                Credentials existingCredentials = credentialsRepo.findById(updatedCredentials.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Credentials not found"));

                if (!existingCredentials.getEmail().equals(updatedCredentials.getEmail())) {
                    boolean emailExists = userService.checkIfEmailExists(updatedCredentials.getEmail());
                    if (emailExists) {
                        return ResponseEntity.status(409).body("Email is already registered.");
                    }
                }

                existingCredentials.setEmail(updatedCredentials.getEmail());
                existingCredentials.setPassword(updatedCredentials.getPassword());

                credentialsRepo.save(existingCredentials);
                existingUser.setCredentials(existingCredentials);
            }

            User savedUser = userService.save(existingUser);
            String newToken = auth.issueToken(savedUser.getUsername());

            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newToken)
                .body(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
