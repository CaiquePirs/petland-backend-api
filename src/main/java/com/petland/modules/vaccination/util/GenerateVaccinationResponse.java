package com.petland.modules.vaccination.util;

import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.AppliedVaccineResponseDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.AppliedVaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateVaccinationResponse {

    public VaccinationResponseDTO generate(Vaccination vaccination) {
        return VaccinationResponseDTO.builder()
                .id(vaccination.getId())
                .petId(vaccination.getPet().getId())
                .customerId(vaccination.getCustomer().getId())
                .veterinarianId(vaccination.getVeterinarian().getId())
                .location(vaccination.getLocation())
                .listAppliedVaccineResponseDTO(mapToAppliedVaccine(vaccination.getAppliedVaccines()))
                .vaccinationDate(vaccination.getVaccinationDate())
                .nextDoseDate(vaccination.getNextDoseDate())
                .clinicalNotes(vaccination.getClinicalNotes())
                .totaByVaccination(vaccination.getTotalByVaccination())
                .build();
    }

    private List<AppliedVaccineResponseDTO> mapToAppliedVaccine(List<AppliedVaccine> listAppliedVaccine) {
        return listAppliedVaccine.stream().map(a -> AppliedVaccineResponseDTO.builder()
                        .id(a.getId())
                        .vaccinationId(a.getVaccination().getId())
                        .vaccineId(a.getVaccine().getId())
                        .quantityUsed(a.getQuantityUsed())
                        .build())
                .toList();
    }
}
