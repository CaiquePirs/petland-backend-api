package com.petland.modules.vaccination.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "DTO used to request a vaccine to be applied")
public record AppliedVaccineRequestDTO(
        @NotNull(message = "Vaccine ID is required")
        @Schema(description = "ID of the vaccine", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID vaccineId,

        @NotNull(message = "Quantity of vaccine used is required")
        @Schema(description = "Quantity of vaccine to apply", example = "1")
        Integer quantityUsed
) {}