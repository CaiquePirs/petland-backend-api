package com.petland.modules.vaccination.dto;

import com.petland.modules.vaccination.enums.VaccineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to create a new vaccine")
public record VaccineRequestDTO(
        @NotBlank(message = "Lot number is required")
        @Schema(description = "Lot number of the vaccine", example = "L12345")
        String lotNumber,

        @NotBlank(message = "Supplier name is required")
        @Schema(description = "Name of the supplier", example = "VaccineCo")
        String supplierName,

        @NotNull(message = "Vaccine Type is required")
        @Schema(description = "Type of the vaccine", example = "RABIES")
        VaccineType vaccineType,

        @NotNull(message = "Purchase price is required")
        @Schema(description = "Purchase price of the vaccine", example = "10.00")
        BigDecimal purchasePrice,

        @NotNull(message = "Price sale is required")
        @Schema(description = "Sale price of the vaccine", example = "15.00")
        BigDecimal priceSale,

        @NotNull(message = "Stock Quantity is required")
        @Schema(description = "Quantity available in stock", example = "100")
        Integer stockQuantity,

        @NotNull(message = "Manufacture Date is required")
        @Schema(description = "Manufacture date of the vaccine", example = "2025-01-01")
        LocalDate manufactureDate,

        @NotNull(message = "Expiration Date is required")
        @Schema(description = "Expiration date of the vaccine", example = "2026-01-01")
        LocalDate expirationDate
) {}
