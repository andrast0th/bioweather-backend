package com.example.bioweatherbackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.scheduling.support.CronExpression;

public class ValidCronValidator implements ConstraintValidator<ValidCron, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return false;
        try {
            CronExpression.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
