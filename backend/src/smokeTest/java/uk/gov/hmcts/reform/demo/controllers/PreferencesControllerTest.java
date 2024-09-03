package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.services.PreferencesService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PreferencesControllerTest {

    @InjectMocks
    private PreferencesController preferencesController;

    @Mock
    private PreferencesService preferencesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPreferences_Success_ReturnsCreatedPreferences() {
        Preferences preferences = new Preferences();
        preferences.setId(1L);
        preferences.setInterests(Arrays.asList("Interest1", "Interest2"));

        when(preferencesService.save(any(Preferences.class))).thenReturn(preferences);

        ResponseEntity<Preferences> response = preferencesController.createPreferences(preferences);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(preferences, response.getBody());
        verify(preferencesService, times(1)).save(any(Preferences.class));
    }

    @Test
    void getAllPreferences_Success_ReturnsAllPreferences() {
        Preferences preferences1 = new Preferences();
        preferences1.setId(1L);
        preferences1.setInterests(Arrays.asList("Interest1", "Interest2"));

        Preferences preferences2 = new Preferences();
        preferences2.setId(2L);
        preferences2.setInterests(Arrays.asList("Interest3", "Interest4"));

        List<Preferences> preferencesList = Arrays.asList(preferences1, preferences2);

        when(preferencesService.findAllPreferences()).thenReturn(preferencesList);

        ResponseEntity<List<Preferences>> response = preferencesController.getAllPreferences();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(preferencesList, response.getBody());
        verify(preferencesService, times(1)).findAllPreferences();
    }
}
