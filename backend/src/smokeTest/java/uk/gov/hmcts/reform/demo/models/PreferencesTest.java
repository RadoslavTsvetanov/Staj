package uk.gov.hmcts.reform.demo.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreferencesTest {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPreferences() {
        Preferences preferences = new Preferences();
        preferences.setInterests(List.of("Reading", "Traveling"));

        Set<ConstraintViolation<Preferences>> violations = validator.validate(preferences);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidPreferencesEmptyInterests() {
        Preferences preferences = new Preferences();
        preferences.setInterests(List.of());

        Set<ConstraintViolation<Preferences>> violations = validator.validate(preferences);
        violations.forEach(violation -> System.out.println(violation.getMessage()));

        assertEquals(2, violations.size());
    }

    @Test
    public void testInvalidPreferencesNullInterests() {
        Preferences preferences = new Preferences();
        preferences.setInterests(null);

        Set<ConstraintViolation<Preferences>> violations = validator.validate(preferences);
        assertEquals(1, violations.size());
        assertEquals("Interests cannot be empty", violations.iterator().next().getMessage());
    }
}
