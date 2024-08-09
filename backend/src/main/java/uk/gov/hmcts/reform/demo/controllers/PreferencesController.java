package uk.gov.hmcts.reform.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.services.UserService;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Preferences createPreferences(@RequestBody Preferences preferences) {
        return userService.savePreferences(preferences);
    }

    @GetMapping("/user/{userId}")
    public Preferences getPreferencesByUserId(@PathVariable Long userId) {
        return userService.getPreferencesByUserId(userId);
    }
}
