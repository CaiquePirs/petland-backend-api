package com.petland.modules.product.dto;

import com.petland.modules.product.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update an existing product. All fields are optional.")
public record ProductUpdateDTO(
        @Schema(description = "Product name", example = "Dog Food")
        String name,

        @Schema(description = "Product description", example = "Premium dry dog food, 10kg")
        String description,

        @Schema(description = "Brand of the product", example = "PetBrand")
        String brand,

        @Schema(description = "Name of the supplier", example = "PetSupply Inc.")
        String supplierName,

        @Schema(description = "Type of the product", example = "FOOD")
        ProductType productType,

        @Schema(description = "Date of manufacture", example = "2025-01-01")
        LocalDate manufactureDate,

        @Schema(description = "Expiration date", example = "2026-01-01")
        LocalDate expirationDate,

        @Schema(description = "Cost price of the product", example = "50.00")
        BigDecimal costPrice,

        @Schema(description = "Sale price of the product", example = "80.00")
        BigDecimal costSale,

        @Schema(description = "Quantity in stock", example = "100")
        Integer stockQuantity
) {}
