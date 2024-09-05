package uk.gov.hmcts.reform.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.repositories.PreferencesRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PreferencesServiceTest {

    @InjectMocks
    private PreferencesService preferencesService;

    @Mock
    private PreferencesRepo preferencesRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllPreferences() {
        Preferences preferences1 = new Preferences();
        preferences1.setId(1L);
        preferences1.setInterests(Arrays.asList("Reading", "Traveling"));

        Preferences preferences2 = new Preferences();
        preferences2.setId(2L);
        preferences2.setInterests(Arrays.asList("Cooking", "Cycling"));

        List<Preferences> preferencesList = Arrays.asList(preferences1, preferences2);

        when(preferencesRepo.findAll()).thenReturn(preferencesList);

        List<Preferences> result = preferencesService.findAllPreferences();

        assertEquals(2, result.size());
        assertEquals(preferences1, result.get(0));
        assertEquals(preferences2, result.get(1));
    }

    @Test
    public void testSavePreferences() {
        Preferences preferences = new Preferences();
        preferences.setId(1L);
        preferences.setInterests(Arrays.asList("Reading", "Traveling"));

        when(preferencesRepo.save(any(Preferences.class))).thenReturn(preferences);

        Preferences result = preferencesService.save(preferences);

        assertEquals(preferences.getId(), result.getId());
        assertEquals(preferences.getInterests(), result.getInterests());
    }
}
