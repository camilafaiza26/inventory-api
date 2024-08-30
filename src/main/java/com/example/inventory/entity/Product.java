package com.example.inventory.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import jakarta.persistence.Convert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", allocationSize = 1)
    @Column(name = "id_product")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "serial_number", nullable = false, unique = false)
    private String serialNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_info", columnDefinition = "JSONB")
    private String additionalInfo;


    @Column(name = "image_path", nullable = true)
    private String imagePath;

//    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;

//    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

}
