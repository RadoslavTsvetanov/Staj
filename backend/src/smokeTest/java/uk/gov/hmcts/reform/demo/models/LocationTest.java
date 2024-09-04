package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

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
        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");
        location.setAgeRestriction(true);
        location.setType("Type A");
        location.setCost(2);
        location.setDay(10);

        String json = objectMapper.writeValueAsString(location);

        assertNotNull(json);
        assertTrue(json.contains("Test Location"));
        assertTrue(json.contains("Type A"));
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"id\": 1, \"name\": \"Test Location\", \"ageRestriction\": true, \"type\": \"Type A\", \"cost\": 2, \"day\": 10 }";

        Location location = objectMapper.readValue(json, Location.class);

        assertNotNull(location);
        assertEquals(1L, location.getId());
        assertEquals("Test Location", location.getName());
        assertEquals(true, location.getAgeRestriction());
        assertEquals("Type A", location.getType());
        assertEquals(2, location.getCost());
        assertEquals(10, location.getDay());
    }

    @Test
    void testValidationSuccess() {
        Location location = new Location();
        location.setName("Valid Location");
        location.setAgeRestriction(true);
        location.setType("Type A");
        location.setCost(2);
        location.setDay(10);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(location, "location");
        validator.validate(location, errors);

        assertFalse(errors.hasErrors(), "Location should pass validation");
    }

    @Test
    void testValidationFailure_NameBlank() {
        Location location = new Location();
        location.setName("");
        location.setAgeRestriction(true);
        location.setType("Type A");
        location.setCost(2);
        location.setDay(10);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(location, "location");
        validator.validate(location, errors);

        assertTrue(errors.hasFieldErrors("name"), "Name cannot be blank");
    }

    @Test
    void testValidationFailure_CostOutOfBounds() {
        Location location = new Location();
        location.setName("Test Location");
        location.setAgeRestriction(true);
        location.setType("Type A");
        location.setCost(5);
        location.setDay(10);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(location, "location");
        validator.validate(location, errors);

        assertTrue(errors.hasFieldErrors("cost"), "Cost must be between 0 and 4");
    }

    @Test
    void testValidationFailure_DayOutOfBounds() {
        Location location = new Location();
        location.setName("Test Location");
        location.setAgeRestriction(true);
        location.setType("Type A");
        location.setCost(2);
        location.setDay(30);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(location, "location");
        validator.validate(location, errors);

        assertTrue(errors.hasFieldErrors("day"), "Day must be between 1 and 28");
    }

    @Test
    void testValidationFailure_TypeBlank() {
        Location location = new Location();
        location.setName("Test Location");
        location.setAgeRestriction(true);
        location.setType("");
        location.setCost(2);
        location.setDay(10);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(location, "location");
        validator.validate(location, errors);

        assertTrue(errors.hasFieldErrors("type"), "Type cannot be blank");
    }
}
