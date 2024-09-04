package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTest {

    private ObjectMapper objectMapper;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        Place place = new Place();
        place.setId(1L);
        place.setName("Central Park");

        String json = objectMapper.writeValueAsString(place);

        assertNotNull(json);
        assertTrue(json.contains("Central Park"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"name\": \"Central Park\" }";

        Place place = objectMapper.readValue(json, Place.class);

        assertNotNull(place);
        assertEquals(1L, place.getId());
        assertEquals("Central Park", place.getName());
    }

    @Test
    void testValidationSuccess() {
        Place place = new Place();
        place.setName("Central Park");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(place, "place");
        validator.validate(place, errors);

        assertFalse(errors.hasErrors(), "Place should pass validation");
    }

    @Test
    void testValidationFailure_NameBlank() {
        Place place = new Place();
        place.setName(""); // Invalid name

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(place, "place");
        validator.validate(place, errors);

        assertTrue(errors.hasFieldErrors("name"), "Name cannot be blank");
    }

    @Test
    void testSettingPlanAndDateWindow() {
        Plan plan = new Plan();
        DateWindow dateWindow = new DateWindow();

        Place place = new Place();
        place.setPlan(plan);
        place.setDateWindow(dateWindow);

        assertEquals(plan, place.getPlan());
        assertEquals(dateWindow, place.getDateWindow());
    }

    @Test
    void testAddingPlaceLocations() {
        Place place = new Place();

        PlaceLocation location1 = new PlaceLocation();
        PlaceLocation location2 = new PlaceLocation();

        List<PlaceLocation> placeLocations = new ArrayList<>();
        placeLocations.add(location1);
        placeLocations.add(location2);

        place.setPlaceLocations(placeLocations);

        assertEquals(2, place.getPlaceLocations().size());
        assertEquals(location1, place.getPlaceLocations().get(0));
        assertEquals(location2, place.getPlaceLocations().get(1));
    }

    @Test
    void testRemovePlaceLocation() {
        Place place = new Place();

        PlaceLocation location1 = new PlaceLocation();
        PlaceLocation location2 = new PlaceLocation();

        List<PlaceLocation> placeLocations = new ArrayList<>();
        placeLocations.add(location1);
        placeLocations.add(location2);

        place.setPlaceLocations(placeLocations);
        place.getPlaceLocations().remove(location1);

        assertEquals(1, place.getPlaceLocations().size());
        assertEquals(location2, place.getPlaceLocations().get(0));
    }
}
