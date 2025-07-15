package com.petland.modules.product.model;

import com.petland.enums.StatusEntity;
import com.petland.modules.product.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String supplierName;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private UUID barCode = UUID.randomUUID();

    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private BigDecimal costSale;

    @Column(nullable = false)
    private int stockQuantity;

    private StatusEntity status;
    private UUID employee_audit;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
