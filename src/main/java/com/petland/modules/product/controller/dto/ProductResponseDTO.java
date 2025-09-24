package com.petland.modules.product.controller.dto;

import com.petland.modules.product.model.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Schema(description = "DTO used for complete product response")
public record ProductResponseDTO(
        @Schema(description = "Unique ID of the product", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Product name", example = "Dog Food")
        String name,

        @Schema(description = "Product description", example = "Premium dry dog food, 10kg")
        String description,

        @Schema(description = "Brand of the product", example = "PetBrand")
        String brand,

        @Schema(description = "Supplier name", example = "PetSupply Inc.")
        String supplierName,

        @Schema(description = "Type of the product", example = "FOOD")
        ProductType productType,

        @Schema(description = "Date of manufacture", example = "2025-01-01")
        LocalDate manufactureDate,

        @Schema(description = "Expiration date", example = "2026-01-01")
        LocalDate expirationDate,

        @Schema(description = "Barcode of the product", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID barCode,

        @Schema(description = "Cost price of the product", example = "50.00")
        BigDecimal costPrice,

        @Schema(description = "Sale price of the product", example = "80.00")
        BigDecimal costSale,

        @Schema(description = "Quantity in stock", example = "100")
        int stockQuantity,

        @Schema(description = "ID of the employer who registered the product", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID employerId
) {}

