package uk.gov.hmcts.reform.demo.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.gov.hmcts.reform.demo.models.DateWindow;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

        LocalDate startDate = dateWindow.getStartDate();
        LocalDate endDate = dateWindow.getEndDate();

        if (startDate != null && startDate.isBefore(LocalDate.now())) {
            context.buildConstraintViolationWithTemplate("Start date cannot be in the past.")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
            isValid = false;
        }

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                context.buildConstraintViolationWithTemplate("End date must be after the start date.")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
                isValid = false;
            } else {
                long duration = ChronoUnit.DAYS.between(startDate, endDate);
                if (duration > 28) {
                    context.buildConstraintViolationWithTemplate("The duration between start and end dates cannot exceed 28 days.")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
