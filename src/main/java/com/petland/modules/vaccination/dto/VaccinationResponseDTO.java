package com.petland.modules.vaccination.dto;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response DTO representing a vaccination record.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationResponseDTO {

    @Schema(description = "Unique identifier of the vaccination.", example = "f1a2b3c4-d5e6-7f8g-9h0i-123456abcdef")
    private UUID id;

    @Schema(description = "Unique identifier of the pet.", example = "b2a1c3d4-e5f6-7g8h-9i0j-abcdef654321")
    private UUID petId;

    @Schema(description = "Unique identifier of the customer.", example = "a1c2b3d4-e5f6-7g8h-9i0j-abcdef123456")
    private UUID customerId;

    @Schema(description = "Unique identifier of the veterinarian responsible.", example = "c3b2a1d4-e5f6-7g8h-9i0j-abcdef987654")
    private UUID veterinarianId;

    @Schema(description = "List of applied vaccines.")
    private List<AppliedVaccineResponseDTO> listAppliedVaccineResponseDTO;

    @Schema(description = "Date of vaccination.", example = "2025-09-01")
    private LocalDate vaccinationDate;

    @Schema(description = "Date of the next scheduled dose.", example = "2026-03-01")
    private LocalDate nextDoseDate;

    @Schema(description = "Location where the vaccination was performed.")
    private Address location;

    @Schema(description = "Clinical notes related to the vaccination.", example = "The pet responded well to the vaccine. No adverse effects.")
    private String clinicalNotes;

    @Schema(description = "Total cost of the vaccination.", example = "120.00")
    private BigDecimal totaByVaccination;
}
