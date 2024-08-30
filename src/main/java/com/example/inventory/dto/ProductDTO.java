package com.example.inventory.dto;

import com.example.inventory.utils.validation.FileType;
import com.example.inventory.utils.validation.ValidDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

public class ProductDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long id;
        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 100, message = "Product name cannot exceed 100 characters")
        private String productName;
        @Positive(message = "Stock must be a positive number")
        private int stock;
        @NotBlank(message = "Serial number cannot be blank")
        private String serialNumber;
        private String additionalInfo;
        @FileType(allowed = {"jpg", "jpeg", "png"}, message = "Only JPG, JPEG, and PNG files are allowed")
        private MultipartFile image;
        @ValidDateTime(pattern = "yyyy-MM-dd HH:mm:ss", message = "Created at must be in the format yyyy-MM-dd HH:mm:ss")
        private String createdAt;
        private String createdBy;
        @ValidDateTime(pattern = "yyyy-MM-dd HH:mm:ss", message = "Updated at must be in the format yyyy-MM-dd HH:mm:ss")
        private String updatedAt;
        private String updatedBy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String productName;
        private int stock;
        private String serialNumber;
        private Map<String, Object> additionalInfo;
        private String image;
        private String createdAt;
        private String createdBy;
        private String updatedAt;
        private String updatedBy;
    }
}
