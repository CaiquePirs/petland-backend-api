package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.sale.exceptions.InsufficientStockException;
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

    private final VaccineMapper mapper;
    private final VaccineRepository vaccineRepository;

    public Vaccine create(VaccineRequestDTO vaccineRequestDTO){
        return vaccineRepository.save(mapper.toEntity(vaccineRequestDTO));
    }

    public Vaccine findById(UUID vaccineId){
        return vaccineRepository.findById(vaccineId)
                .filter(v -> !v.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Vaccine ID not found"));
    }

    public Vaccine updateVaccineStock(int stockUsed, UUID vaccineId){
        Vaccine vaccine = findById(vaccineId);

        if(vaccine.getStockQuantity() <= 0 || stockUsed > vaccine.getStockQuantity()){
            throw new InsufficientStockException("Vaccine stock is insufficient");
        }

        int stockUpdated = vaccine.getStockQuantity() - stockUsed;

        vaccine.setStockQuantity(stockUpdated);
        return vaccineRepository.save(vaccine);
    }
}
