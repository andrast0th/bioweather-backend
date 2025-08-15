package com.example.bioweatherbackend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidCronValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCron {
    String message() default "Invalid cron expression";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
