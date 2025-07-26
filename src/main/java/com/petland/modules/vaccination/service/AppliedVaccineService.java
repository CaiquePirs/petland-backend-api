package com.petland.modules.vaccination.service;

import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.module.AppliedVaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppliedVaccineService {

    private final VaccineService vaccineService;

    public List<AppliedVaccine> create(Vaccination vaccination, List<AppliedVaccineRequestDTO> listAppliedVaccineRequest){
        List<AppliedVaccine> listVaccineApplied = listAppliedVaccineRequest.stream()
                .map(appliedVaccineRequest -> {
                    AppliedVaccine appliedVaccine = new AppliedVaccine();
                    Vaccine vaccine = vaccineService.updateVaccineStock(appliedVaccineRequest.quantityUsed(), appliedVaccineRequest.vaccineId());
                    appliedVaccine.setVaccination(vaccination);
                    vaccination.getAppliedVaccines().add(appliedVaccine);

                    appliedVaccine.setVaccine(vaccine);
                    appliedVaccine.setQuantityUsed(appliedVaccineRequest.quantityUsed());
                    return appliedVaccine;
                }).collect(Collectors.toList());

        vaccination.setAppliedVaccines(listVaccineApplied);
        return listVaccineApplied;
    }

}
