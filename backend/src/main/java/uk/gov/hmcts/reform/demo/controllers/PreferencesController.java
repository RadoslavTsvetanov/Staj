package uk.gov.hmcts.reform.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.services.PreferencesService;

import java.util.List;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private PreferencesService preferencesService;

    @PostMapping
    public ResponseEntity<Preferences> createPreferences(@Valid @RequestBody Preferences preferences) {
        Preferences savedPreferences = preferencesService.save(preferences);
        return ResponseEntity.ok(savedPreferences);
    }

    @GetMapping
    public ResponseEntity<List<Preferences>> getAllPreferences() {
        return ResponseEntity.ok(preferencesService.findAllPreferences());
    }
}
