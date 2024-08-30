package com.example.inventory.controller;

import com.example.inventory.dto.BaseResponse;
import com.example.inventory.dto.ProductDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/create")
    public BaseResponse<ProductDTO.Response> createProduct(@ModelAttribute @Valid ProductDTO.Request productDTORequest) throws IOException {
        String imagePath = saveImage(productDTORequest.getImage());
        Product product = productMapper.toProduct(productDTORequest);
        product.setImagePath(imagePath);
        ProductDTO.Response responseDTO = productMapper.toProductDTO(productService.createProduct(product));
        return BaseResponse.<ProductDTO.Response>builder()
                .code("200")
                .data(responseDTO)
                .build();
    }



    @GetMapping("/list")
    public BaseResponse<List<ProductDTO.Response>> getListProducts() {
        List<Product> products = productService.getListProducts();
        List<ProductDTO.Response> responseDTOs = products.stream()
                .map(productMapper::toProductDTO)
                .collect(Collectors.toList());
        return BaseResponse.<List<ProductDTO.Response>>builder()
                .code("200")
                .data(responseDTOs)
                .build();
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<ProductDTO.Response> getProductDetail(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return BaseResponse.<ProductDTO.Response>builder()
                    .code("404")
                    .message("Product not found")
                    .build();
        }
        ProductDTO.Response responseDTO = productMapper.toProductDTO(product.orElse(null));
        return BaseResponse.<ProductDTO.Response>builder()
                .code("200")
                .data(responseDTO)
                .build();
    }


    @PutMapping("/update/{id}")
    public BaseResponse<ProductDTO.Response> updateProduct(@PathVariable("id") Long id,
                                                           @ModelAttribute @Valid ProductDTO.Request productDTORequest) throws IOException {

        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isEmpty()) {
            return BaseResponse.<ProductDTO.Response>builder()
                    .code("404")
                    .message("Product not found")
                    .build();
        }
        Product product = existingProduct.get();
        String oldImagePath = product.getImagePath();
        String newImagePath = productDTORequest.getImage() != null ? saveImage(productDTORequest.getImage()) : oldImagePath;
        if (productDTORequest.getImage() != null && !oldImagePath.equals(newImagePath)) {
            deleteImage(oldImagePath);
        }
        product = productMapper.toProduct(productDTORequest);
        product.setId(id); // Ensure the existing ID is retained
        product.setImagePath(newImagePath);

        ProductDTO.Response responseDTO = productMapper.toProductDTO(productService.updateProduct(product));
        return BaseResponse.<ProductDTO.Response>builder()
                .code("200")
                .data(responseDTO)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Void> deleteProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return BaseResponse.<Void>builder()
                    .code("404")
                    .message("Product not found")
                    .build();
        }
        Product existingProduct = product.get();
        String imagePath = existingProduct.getImagePath();
        productService.deleteProduct(id);
        if (imagePath != null && !imagePath.isEmpty()) {
            deleteImage(imagePath);
        }
        return BaseResponse.<Void>builder()
                .code("200")
                .build();
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String originalFileName = image.getOriginalFilename();
            String uniqueFileName =  UUID.randomUUID()+originalFileName ;
            Path path = Paths.get(uploadDir, uniqueFileName);
            Files.write(path, image.getBytes());
            return path.toString();
        }
        return null;
    }

    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
