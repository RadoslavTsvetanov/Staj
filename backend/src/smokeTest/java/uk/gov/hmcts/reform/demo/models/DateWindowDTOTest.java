package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateWindowDTOTest {

    private ObjectMapper objectMapper;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    void testValidationSuccess() {
        DateWindowDTO dateWindowDTO = new DateWindowDTO(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindowDTO, "dateWindowDTO");
        validator.validate(dateWindowDTO, errors);

        assertFalse(errors.hasErrors(), "DateWindowDTO should pass validation");
    }

    @Test
    void testValidationFailure_StartDateInPast() {
        DateWindowDTO dateWindowDTO = new DateWindowDTO(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindowDTO, "dateWindowDTO");
        validator.validate(dateWindowDTO, errors);

        assertTrue(errors.hasFieldErrors("startDate"), "Start date in the past should fail validation");
    }

    @Test
    void testValidationFailure_EndDateInPast() {
        DateWindowDTO dateWindowDTO = new DateWindowDTO(LocalDate.now().plusDays(1), LocalDate.now().minusDays(1));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindowDTO, "dateWindowDTO");
        validator.validate(dateWindowDTO, errors);

        assertTrue(errors.hasFieldErrors("endDate"), "End date in the past should fail validation");
    }

    @Test
    void testValidationFailure_StartDateNull() {
        DateWindowDTO dateWindowDTO = new DateWindowDTO(null, LocalDate.now().plusDays(5));

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindowDTO, "dateWindowDTO");
        validator.validate(dateWindowDTO, errors);

        assertTrue(errors.hasFieldErrors("startDate"), "Start date being null should fail validation");
    }

    @Test
    void testValidationFailure_EndDateNull() {
        DateWindowDTO dateWindowDTO = new DateWindowDTO(LocalDate.now().plusDays(1), null);

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dateWindowDTO, "dateWindowDTO");
        validator.validate(dateWindowDTO, errors);

        assertTrue(errors.hasFieldErrors("endDate"), "End date being null should fail validation");
    }
}
