package uk.gov.hmcts.reform.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {

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
        Memory memory = new Memory();
        memory.setImage("image_url");
        memory.setDate(LocalDate.now());
        memory.setPlace("Paris");
        memory.setDescription("A wonderful memory in Paris.");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(memory, "memory");
        validator.validate(memory, errors);

        assertFalse(errors.hasErrors(), "Memory should pass validation");
    }

    @Test
    void testValidationFailure_ImageBlank() {
        Memory memory = new Memory();
        memory.setImage(""); // Invalid image
        memory.setDate(LocalDate.now());
        memory.setPlace("Paris");
        memory.setDescription("A wonderful memory in Paris.");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(memory, "memory");
        validator.validate(memory, errors);

        assertTrue(errors.hasFieldErrors("image"), "Image cannot be blank");
    }

    @Test
    void testValidationFailure_DateNull() {
        Memory memory = new Memory();
        memory.setImage("image_url");
        memory.setDate(null);
        memory.setPlace("Paris");
        memory.setDescription("A wonderful memory in Paris.");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(memory, "memory");
        validator.validate(memory, errors);

        assertTrue(errors.hasFieldErrors("date"), "Date cannot be null");
    }

    @Test
    void testValidationFailure_PlaceBlank() {
        Memory memory = new Memory();
        memory.setImage("image_url");
        memory.setDate(LocalDate.now());
        memory.setPlace("");
        memory.setDescription("A wonderful memory in Paris.");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(memory, "memory");
        validator.validate(memory, errors);

        assertTrue(errors.hasFieldErrors("place"), "Place cannot be blank");
    }

    @Test
    void testValidationFailure_DescriptionTooLong() {
        Memory memory = new Memory();
        memory.setImage("image_url");
        memory.setDate(LocalDate.now());
        memory.setPlace("Paris");
        memory.setDescription("A".repeat(501)); // Invalid description (too long)

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(memory, "memory");
        validator.validate(memory, errors);

        assertTrue(errors.hasFieldErrors("description"), "Description cannot be longer than 500 characters");
    }
}
