package com.petland.modules.vaccination.service;

import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
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

    public List<AppliedVaccine> create(List<AppliedVaccineRequestDTO> appliedVaccinesDTO) {
        return appliedVaccinesDTO.stream()
                .map(dto -> {
                    AppliedVaccine appliedVaccine = new AppliedVaccine();
                    Vaccine vaccine = vaccineService.updateStock(dto.quantityUsed(), dto.vaccineId());
                    appliedVaccine.setVaccine(vaccine);
                    appliedVaccine.setQuantityUsed(dto.quantityUsed());
                    return appliedVaccine;
                }).collect(Collectors.toList());
    }

}
