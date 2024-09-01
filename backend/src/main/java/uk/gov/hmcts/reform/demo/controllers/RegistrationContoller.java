package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.Role;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.repositories.CredentialsRepo;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;
import uk.gov.hmcts.reform.demo.repositories.UserRepo;
import jakarta.validation.Valid;
import uk.gov.hmcts.reform.demo.validators.ValidAge;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class RegistrationContoller {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CredentialsRepo credentialsRepo;

    @Autowired
    private PreferencesRepo preferencesRepo;

    @PostMapping("/register/basic")
    public ResponseEntity<String> registerBasicInfo(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam String email
    ) {
        if (userRepo.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (credentialsRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Credentials credentials = new Credentials();
        credentials.setPassword(password);
        credentials.setEmail(email);
        credentialsRepo.save(credentials);

        User user = new User();
        user.setUsername(username);
        user.setCredentials(credentials);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return ResponseEntity.ok("Basic information registered. Proceed to complete registration.");
    }

    @PostMapping("/register/complete")
    public ResponseEntity<String> completeRegistration(
        @RequestParam String username,
        @RequestParam @Valid LocalDate dateOfBirth,
        @RequestParam String name,
        @RequestParam List<String> interests
    ) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        user.setName(name);
        user.setDateOfBirth(dateOfBirth);

        Preferences preferences = new Preferences();
        preferences.setInterests(interests);
        preferencesRepo.save(preferences);

        user.setPreferences(preferences);
        userRepo.save(user);

        return ResponseEntity.ok("Registration complete");
    }
}
