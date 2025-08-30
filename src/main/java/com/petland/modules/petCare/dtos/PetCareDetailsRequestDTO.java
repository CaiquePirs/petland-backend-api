package com.petland.modules.petCare.dtos;

import com.petland.modules.petCare.enums.PetCareType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record PetCareDetailsRequestDTO(
        @NotNull(message = "Service type is required")
        PetCareType petCareType,

        @NotNull(message = "Unit price is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
        BigDecimal unitPrice,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantityService,

        @NotNull(message = "Operating cost is required.")
        @DecimalMin(value = "0.0", inclusive = true, message = "Operating cost must be zero or greater")
        BigDecimal operatingCost,

        @Size(max = 500, message = "Service notes must be at most 500 characters")
        String noteService) {
}
