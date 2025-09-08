package com.petland.modules.petCare.dtos;

import com.petland.modules.petCare.enums.PetCareType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
@Schema(description = "DTO used to create details of a pet care service")
public record PetCareDetailsRequestDTO(
        @NotNull(message = "Service type is required")
        @Schema(description = "Type of pet care service", example = "GROOMING")
        PetCareType petCareType,

        @NotNull(message = "Unit price is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
        @Schema(description = "Unit price of the service", example = "50.00")
        BigDecimal unitPrice,

        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "Quantity of the service", example = "1")
        int quantityService,

        @NotNull(message = "Operating cost is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Operating cost must be zero or greater")
        @Schema(description = "Operating cost of the service", example = "10.00")
        BigDecimal operatingCost,

        @Size(max = 500, message = "Service notes must be at most 500 characters")
        @Schema(description = "Notes about the service", example = "Use hypoallergenic shampoo", maxLength = 500)
        String noteService
) {}
