package uk.gov.hmcts.reform.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.gov.hmcts.reform.demo.models.Preferences;
import uk.gov.hmcts.reform.demo.services.PreferencesService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PreferencesController.class)
public class PreferencesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PreferencesService preferencesService;

    @InjectMocks
    private PreferencesController preferencesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(preferencesController).build();
    }

    @Test
    public void testCreatePreferences() throws Exception {
        Preferences preferences = new Preferences();
        preferences.setId(1L);
        preferences.setInterests(List.of("Reading", "Traveling"));

        when(preferencesService.save(any(Preferences.class))).thenReturn(preferences);

        mockMvc.perform(post("/preferences")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"interests\": [\"Reading\", \"Traveling\"]}"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.interests[0]").value("Reading"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.interests[1]").value("Traveling"));
    }

    @Test
    public void testGetAllPreferences() throws Exception {
        Preferences preferences1 = new Preferences();
        preferences1.setId(1L);
        preferences1.setInterests(List.of("Reading", "Traveling"));

        Preferences preferences2 = new Preferences();
        preferences2.setId(2L);
        preferences2.setInterests(List.of("Cooking", "Cycling"));

        List<Preferences> preferencesList = List.of(preferences1, preferences2);

        when(preferencesService.findAllPreferences()).thenReturn(preferencesList);

        mockMvc.perform(get("/preferences")
                            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].interests[0]").value("Reading"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].interests[0]").value("Cooking"));
    }
}
