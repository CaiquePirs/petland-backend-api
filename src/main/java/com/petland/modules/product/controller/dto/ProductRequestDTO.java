package com.petland.modules.product.controller.dto;

import com.petland.modules.product.model.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to create a new product")
public record ProductRequestDTO(
        @NotBlank(message = "Name is required")
        @Schema(description = "Product name", example = "Dog Food")
        String name,

        @NotBlank(message = "Description is required")
        @Schema(description = "Product description", example = "Premium dry dog food, 10kg")
        String description,

        @NotBlank(message = "Brand is required")
        @Schema(description = "Brand of the product", example = "PetBrand")
        String brand,

        @NotBlank(message = "Supplier name is required")
        @Schema(description = "Name of the supplier", example = "PetSupply Inc.")
        String supplierName,

        @NotNull(message = "Product type is required")
        @Schema(description = "Type of the product", example = "FOOD")
        ProductType productType,

        @NotNull(message = "Manufacture date is required")
        @Schema(description = "Date of manufacture", example = "2025-01-01")
        LocalDate manufactureDate,

        @NotNull(message = "Expiration date is required")
        @Schema(description = "Expiration date", example = "2026-01-01")
        LocalDate expirationDate,

        @NotNull(message = "Cost price is required")
        @Schema(description = "Cost price of the product", example = "50.00")
        BigDecimal costPrice,

        @NotNull(message = "Cost sale is required")
        @Schema(description = "Sale price of the product", example = "80.00")
        BigDecimal costSale,

        @NotNull(message = "Stock quantity is required")
        @Schema(description = "Quantity in stock", example = "100")
        int stockQuantity
) {}

