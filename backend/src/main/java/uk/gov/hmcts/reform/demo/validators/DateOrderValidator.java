package uk.gov.hmcts.reform.demo.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.gov.hmcts.reform.demo.models.DateWindow;

import java.time.LocalDate;

public class DateOrderValidator implements ConstraintValidator<ValidDateWindow, DateWindow> {

    @Override
    public void initialize(ValidDateWindow constraintAnnotation) {
    }

    @Override
    public boolean isValid(DateWindow dateWindow, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (dateWindow == null) {
            return true;
        }

        if (dateWindow.getStartDate() != null && dateWindow.getStartDate().isBefore(LocalDate.now())) {
            context.buildConstraintViolationWithTemplate("Start date cannot be in the past.")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            isValid = false;
        }

        if (dateWindow.getStartDate() != null && dateWindow.getEndDate() != null) {
            if (dateWindow.getStartDate().isAfter(dateWindow.getEndDate())) {
                context.buildConstraintViolationWithTemplate("End date must be after the start date.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}
