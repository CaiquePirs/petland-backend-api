package com.petland.modules.petCare.controller.dtos;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "DTO used to create a pet care service")
public record PetCareRequestDTO(
        @NotNull(message = "Pet ID is required")
        @Schema(description = "ID of the pet", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID petId,

        @NotNull(message = "Customer ID is required")
        @Schema(description = "ID of the customer", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID customerId,

        @NotNull(message = "Employee ID is required")
        @Schema(description = "ID of the employee providing the service", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID employeeId,

        @NotNull(message = "Services details is required")
        @Schema(description = "List of pet care service details")
        List<PetCareDetailsRequestDTO> serviceDetailsList,

        @NotNull(message = "Location is required")
        @Schema(description = "Location where the service is provided")
        Address location
) {}
