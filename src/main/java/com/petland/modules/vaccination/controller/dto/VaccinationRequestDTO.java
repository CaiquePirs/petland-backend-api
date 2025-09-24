package com.petland.modules.vaccination.controller.dto;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "DTO used to create a vaccination record for a pet")
public record VaccinationRequestDTO(
        @NotNull(message = "Pet ID is required")
        @Schema(description = "ID of the pet receiving the vaccination", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID petId,

        @NotNull(message = "Owner Id is required")
        @Schema(description = "ID of the customer/owner", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID customerId,

        @NotNull(message = "Veterinarian ID is required")
        @Schema(description = "ID of the veterinarian applying the vaccine", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID veterinarianId,

        @NotNull(message = "Vaccine applied is required")
        @Schema(description = "List of vaccines applied")
        List<AppliedVaccineRequestDTO> listAppliedVaccineRequestDTO,

        @NotNull(message = "Vaccination date is required")
        @Schema(description = "Date when the vaccination was applied", example = "2025-09-08")
        LocalDate vaccinationDate,

        @Schema(description = "Date of the next dose, if any", example = "2026-03-08")
        LocalDate nextDoseDate,

        @NotNull(message = "Vaccination location is required")
        @Schema(description = "Location where the vaccination was performed")
        Address location,

        @Schema(description = "Clinical notes", example = "Patient is healthy and calm")
        String clinicalNotes
) {}

