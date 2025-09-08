package com.petland.modules.vaccination.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update a vaccination record")
public record VaccinationUpdateDTO(
        @Schema(description = "Updated location of the vaccination")
        UpdateAddressDTO location,

        @Schema(description = "Updated clinical notes", example = "Patient showed mild reaction")
        String clinicalNotes,

        @Schema(description = "Updated vaccination date", example = "2025-09-08")
        LocalDate vaccinationDate,

        @Schema(description = "Updated next dose date", example = "2026-03-08")
        LocalDate nextDoseDate
) {}
