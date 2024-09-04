package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateWindowTest {

    private ObjectMapper objectMapper;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();  // Initialize the validator
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        String json = "{ \"startDate\": \"2024-09-01\", \"endDate\": \"2024-09-10\" }";

        DateWindow dateWindow = objectMapper.readValue(json, DateWindow.class);

        assertNotNull(dateWindow);
        assertEquals(LocalDate.of(2024, 9, 1), dateWindow.getStartDate());
        assertEquals(LocalDate.of(2024, 9, 10), dateWindow.getEndDate());
    }

    @Test
    void testValidationSuccess() {
        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(LocalDate.now().plusDays(1));
        dateWindow.setEndDate(LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindow, "dateWindow");
        validator.validate(dateWindow, errors);

        assertFalse(errors.hasErrors(), "DateWindow should pass validation");
    }

    @Test
    void testValidationFailure_StartDateInPast() {
        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(LocalDate.now().minusDays(1));
        dateWindow.setEndDate(LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindow, "dateWindow");
        validator.validate(dateWindow, errors);

        assertTrue(errors.hasFieldErrors("startDate"), "Start date in the past should fail validation");
    }

    @Test
    void testValidationFailure_EndDateInPast() {
        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(LocalDate.now().plusDays(1));
        dateWindow.setEndDate(LocalDate.now().minusDays(1));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindow, "dateWindow");
        validator.validate(dateWindow, errors);

        assertTrue(errors.hasFieldErrors("endDate"), "End date in the past should fail validation");
    }

    @Test
    void testValidationFailure_StartDateNull() {
        DateWindow dateWindow = new DateWindow();
        dateWindow.setEndDate(LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindow, "dateWindow");
        validator.validate(dateWindow, errors);

        assertTrue(errors.hasFieldErrors("startDate"), "Start date being null should fail validation");
    }

    @Test
    void testValidationFailure_EndDateNull() {
        DateWindow dateWindow = new DateWindow();
        dateWindow.setStartDate(LocalDate.now().plusDays(1));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindow, "dateWindow");
        validator.validate(dateWindow, errors);

        assertTrue(errors.hasFieldErrors("endDate"), "End date being null should fail validation");
    }
}
