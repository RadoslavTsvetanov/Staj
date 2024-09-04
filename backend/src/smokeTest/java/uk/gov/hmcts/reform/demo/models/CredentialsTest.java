package uk.gov.hmcts.reform.demo.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.util.Set;

public class CredentialsTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidCredentials() {
        Credentials credentials = new Credentials();
        credentials.setEmail("valid.email@example.com");
        credentials.setPassword("ValidPass123");

        Set<ConstraintViolation<Credentials>> violations = validator.validate(credentials);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidEmail() {
        Credentials credentials = new Credentials();
        credentials.setEmail("invalid-email");
        credentials.setPassword("ValidPass123");

        Set<ConstraintViolation<Credentials>> violations = validator.validate(credentials);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).extracting("message").contains("Email should be valid");
    }

    @Test
    public void testBlankPassword() {
        Credentials credentials = new Credentials();
        credentials.setEmail("valid.email@example.com");
        credentials.setPassword("");

        Set<ConstraintViolation<Credentials>> violations = validator.validate(credentials);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).extracting("message").contains("Password cannot be blank");
    }

    @Test
    public void testShortPassword() {
        Credentials credentials = new Credentials();
        credentials.setEmail("valid.email@example.com");
        credentials.setPassword("short");

        Set<ConstraintViolation<Credentials>> violations = validator.validate(credentials);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations).extracting("message").contains("Password must be at least 8 characters long");
    }

    @Test
    public void testGettersAndSetters() {
        Credentials credentials = new Credentials();
        credentials.setId(1L);
        credentials.setEmail("test.email@example.com");
        credentials.setPassword("Password123");

        Assertions.assertThat(credentials.getId()).isEqualTo(1L);
        Assertions.assertThat(credentials.getEmail()).isEqualTo("test.email@example.com");
        Assertions.assertThat(credentials.getPassword()).isEqualTo("Password123");
    }
}
