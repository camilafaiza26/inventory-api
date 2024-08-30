package com.example.inventory.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

    private String pattern;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(ValidDateTime constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            LocalDateTime.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
