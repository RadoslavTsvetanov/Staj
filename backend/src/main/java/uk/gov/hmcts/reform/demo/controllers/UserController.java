package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Credentials;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.models.User;
import uk.gov.hmcts.reform.demo.services.UserService;

import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createUsers(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/{userId}/credentials")
    public User addCredentialsToUser(@PathVariable Long userId, @RequestBody Credentials credentials) {
        return userService.addCredentialsAndPreferences(userId, credentials, null);
    }

    @PostMapping("/{userId}/preferences")
    public User addPreferencesToUser(@PathVariable Long userId, @RequestBody Preferences preferences) {
        return userService.addCredentialsAndPreferences(userId, null, preferences);
    }
}
