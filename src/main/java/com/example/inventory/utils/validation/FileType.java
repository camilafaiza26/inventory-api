package com.example.inventory.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileType {
    String message() default "Invalid file type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Define the allowed file types
    String[] allowed() default {"jpg", "jpeg", "png"};
}
