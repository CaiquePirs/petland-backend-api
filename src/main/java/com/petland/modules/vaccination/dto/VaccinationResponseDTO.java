package com.petland.modules.vaccination.dto;

import com.petland.common.entity.Address;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationResponseDTO {
    private UUID id;
    private UUID petId;
    private UUID customerId;
    private UUID veterinarianId;
    private List<AppliedVaccineResponseDTO> listAppliedVaccineResponseDTO;
    private LocalDate vaccinationDate;
    private LocalDate nextDoseDate;
    private Address location;
    private String clinicalNotes;
    private BigDecimal totalCostVaccination;
}
