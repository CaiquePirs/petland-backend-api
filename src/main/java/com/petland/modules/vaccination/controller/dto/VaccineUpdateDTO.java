package com.petland.modules.vaccination.controller.dto;

import com.petland.modules.vaccination.module.enums.VaccineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update an existing vaccine")
public record VaccineUpdateDTO(
        @Schema(description = "Lot number of the vaccine", example = "L12345")
        String lotNumber,

        @Schema(description = "Supplier name", example = "VaccineCo")
        String supplierName,

        @Schema(description = "Type of vaccine", example = "RABIES")
        VaccineType vaccineType,

        @Schema(description = "Purchase price", example = "10.00")
        BigDecimal purchasePrice,

        @Schema(description = "Sale price", example = "15.00")
        BigDecimal priceSale,

        @Schema(description = "Quantity in stock", example = "100")
        Integer stockQuantity,

        @Schema(description = "Manufacture date", example = "2025-01-01")
        LocalDate manufactureDate,

        @Schema(description = "Expiration date", example = "2026-01-01")
        LocalDate expirationDate
) {}

