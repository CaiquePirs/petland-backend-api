package com.petland.modules.petCare.controller.dtos;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "DTO used for complete pet care service history response")
public record PetCareHistoryResponseDTO(
        @Schema(description = "Unique ID of the pet care record", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "ID of the pet", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID petId,

        @Schema(description = "ID of the customer", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID customerId,

        @Schema(description = "ID of the employee providing the service", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID employeeId,

        @Schema(description = "List of service details")
        List<PetCareDetailsHistoryResponseDTO> detailsResponseDTO,

        @Schema(description = "Total value of all services", example = "120.00")
        BigDecimal totalService,

        @Schema(description = "Date and time when the service was provided", example = "2025-09-08T14:30:00")
        LocalDateTime serviceDate,

        @Schema(description = "Location where the service was provided")
        Address location
) {}
