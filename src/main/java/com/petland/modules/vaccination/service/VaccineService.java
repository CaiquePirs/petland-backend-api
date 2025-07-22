package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineMapper vaccineMapper;
    private final VaccineRepository vaccineRepository;

    public Vaccine create(VaccineRequestDTO vaccineRequestDTO){
        Vaccine vaccine = vaccineMapper.toEntity(vaccineRequestDTO);
        vaccine.setStatus(StatusEntity.ACTIVE);
        return vaccineRepository.save(vaccine);
    }

    public Vaccine findById(UUID vaccineId){
        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new NotFoundException("Vaccine ID not found"));

        if(vaccine.getStatus().equals(StatusEntity.DELETED)){
            throw new NotFoundException("Vaccine ID not found");
        }
        return vaccine;
    }
}
