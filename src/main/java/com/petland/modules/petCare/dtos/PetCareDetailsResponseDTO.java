package com.petland.modules.petCare.dtos;

import com.petland.modules.petCare.enums.PetCareType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Schema(description = "DTO used for pet care service details response")
public record PetCareDetailsResponseDTO(
        @Schema(description = "Unique ID of the service detail", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Type of pet care service", example = "GROOMING")
        PetCareType petCareType,

        @Schema(description = "Unit price of the service", example = "50.00")
        BigDecimal unitPrice,

        @Schema(description = "Quantity of the service", example = "1")
        int quantityService,

        @Schema(description = "Operating cost of the service", example = "10.00")
        BigDecimal operatingCost,

        @Schema(description = "Total price by service", example = "50.00")
        BigDecimal totalByService,

        @Schema(description = "Notes about the service", example = "Use hypoallergenic shampoo")
        String noteService,

        @Schema(description = "ID of the pet care record this detail belongs to", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID petCareId
) {}
