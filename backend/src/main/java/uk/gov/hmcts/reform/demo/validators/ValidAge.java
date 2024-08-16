package uk.gov.hmcts.reform.demo.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAge {
    String message() default "User must be at least 13 years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
