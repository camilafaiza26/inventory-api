package com.example.inventory.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    private String[] allowedFileTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedFileTypes = constraintAnnotation.allowed();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return Arrays.asList(allowedFileTypes).contains(extension);
    }
}
