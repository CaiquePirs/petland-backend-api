package com.petland.modules.vaccination.util;

import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.AppliedVaccineResponseDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.AppliedVaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateVaccinationResponse {

    public VaccinationResponseDTO generate(Vaccination vaccination){
        List<AppliedVaccineResponseDTO> listAppliedVaccines = generateAppliedVaccineListResponse(vaccination.getAppliedVaccines());

        VaccinationResponseDTO responseDTO = VaccinationResponseDTO.builder()
                .id(vaccination.getId())
                .petId(vaccination.getPet().getId())
                .customerId(vaccination.getCustomer().getId())
                .veterinarianId(vaccination.getVeterinarian().getId())
                .location(vaccination.getLocation())
                .listAppliedVaccineResponseDTO(listAppliedVaccines)
                .vaccinationDate(vaccination.getVaccinationDate())
                .nextDoseDate(vaccination.getNextDoseDate())
                .clinicalNotes(vaccination.getClinicalNotes())
                .totaByVaccination(vaccination.getTotalByVaccination())
                .build();
        return responseDTO;
    }

    public List<AppliedVaccineResponseDTO> generateAppliedVaccineListResponse(List<AppliedVaccine> listAppliedVaccine){
        List<AppliedVaccineResponseDTO> listAppliedVaccineDTO = new ArrayList<>();

        for(AppliedVaccine appliedVaccine : listAppliedVaccine){
            AppliedVaccineResponseDTO listResponse = AppliedVaccineResponseDTO.builder()
                    .id(appliedVaccine.getId())
                    .vaccinationId(appliedVaccine.getVaccination().getId())
                    .vaccineId(appliedVaccine.getVaccine().getId())
                    .quantityUsed(appliedVaccine.getQuantityUsed())
                    .build();
            listAppliedVaccineDTO.add(listResponse);
        }
        return listAppliedVaccineDTO;
    }
}
