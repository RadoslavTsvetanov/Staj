package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class PlaceLocationTest {

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
        Location location = new Location();
        location.setId(2L);

        PlaceLocation placeLocation = new PlaceLocation(place, location);
        placeLocation.setId(3L);
        placeLocation.setDay(5);

        String json = objectMapper.writeValueAsString(placeLocation);

        assertNotNull(json);
        assertTrue(json.contains("5"));
        assertTrue(json.contains("3"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 3, \"day\": 5 }";

        PlaceLocation placeLocation = objectMapper.readValue(json, PlaceLocation.class);

        assertNotNull(placeLocation);
        assertEquals(3L, placeLocation.getId());
        assertEquals(5, placeLocation.getDay());
    }

    @Test
    void testValidationSuccess() {
        PlaceLocation placeLocation = new PlaceLocation();
        placeLocation.setDay(15);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(placeLocation, "placeLocation");
        validator.validate(placeLocation, errors);

        assertFalse(errors.hasErrors(), "PlaceLocation should pass validation");
    }

    @Test
    void testValidationFailure_DayBelowMin() {
        PlaceLocation placeLocation = new PlaceLocation();
        placeLocation.setDay(0); // Invalid day, below minimum

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(placeLocation, "placeLocation");
        validator.validate(placeLocation, errors);

        assertTrue(errors.hasFieldErrors("day"), "Day below minimum should fail validation");
    }

    @Test
    void testValidationFailure_DayAboveMax() {
        PlaceLocation placeLocation = new PlaceLocation();
        placeLocation.setDay(29); // Invalid day, above maximum

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(placeLocation, "placeLocation");
        validator.validate(placeLocation, errors);

        assertTrue(errors.hasFieldErrors("day"), "Day above maximum should fail validation");
    }

    @Test
    void testSettingPlaceAndLocation() {
        Place place = new Place();
        Location location = new Location();

        PlaceLocation placeLocation = new PlaceLocation();
        placeLocation.setPlace(place);
        placeLocation.setLocation(location);

        assertEquals(place, placeLocation.getPlace());
        assertEquals(location, placeLocation.getLocation());
    }
}
