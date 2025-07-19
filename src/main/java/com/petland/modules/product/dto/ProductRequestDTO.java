package com.petland.modules.product.dto;

import com.petland.modules.product.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Brand is required")
        String brand,

        @NotBlank(message = "Supplier name is required")
        String supplierName,

        @NotNull(message = "Product type is required")
        ProductType productType,

        @NotNull(message = "Manufacture date is required")
        LocalDate manufactureDate,

        @NotNull(message = "Expiration date is required")
        LocalDate expirationDate,

        @NotNull(message = "Cost price is required")
        BigDecimal costPrice,

        @NotNull(message = "Cost sale is required")
        BigDecimal costSale,

        @NotNull(message = "Stock quantity is required")
        int stockQuantity) {
}
