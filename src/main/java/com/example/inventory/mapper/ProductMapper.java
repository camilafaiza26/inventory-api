package com.example.inventory.mapper;

import com.example.inventory.dto.ProductDTO;
import com.example.inventory.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "stringToLocalDateTime")
    public abstract Product toProduct(ProductDTO.Request productDTORequest);

    @Mapping(source = "imagePath", target = "image")
    @Mapping(source = "additionalInfo", target = "additionalInfo", qualifiedByName = "stringToMap")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToString")
    public abstract ProductDTO.Response toProductDTO(Product product);

    @Named("stringToMap")
    public Map<String, Object> stringToMap(String additionalInfo) {
        try {
            return objectMapper.readValue(additionalInfo, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON string to Map", e);
        }
    }

    @Named("stringToLocalDateTime")
    public LocalDateTime stringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
                return null;
            }else{
                return LocalDateTime.parse(dateTimeString, formatter);
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Named("localDateTimeToString")
    public String localDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            if (dateTime != null) {
                return dateTime.format(formatter);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
