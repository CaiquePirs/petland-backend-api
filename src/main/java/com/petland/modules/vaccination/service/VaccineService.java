package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InsufficientStockException;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.dto.VaccineUpdateDTO;
import com.petland.modules.vaccination.enums.VaccineType;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccineRepository;
import com.petland.modules.vaccination.specifications.VaccineSpecification;
import com.petland.modules.vaccination.util.VaccineUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineMapper mapper;
    private final VaccineRepository vaccineRepository;
    private final VaccineUpdateValidator validator;

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

    public void deleteById(UUID vaccineId){
        Vaccine vaccine = findById(vaccineId);
        vaccine.setStatus(StatusEntity.DELETED);
        vaccineRepository.save(vaccine);
    }

    public BigDecimal calculateProfitByVaccine(Vaccine vaccine){
        if(vaccine.getPurchasePrice() == null || vaccine.getPriceSale() == null){
            return BigDecimal.ZERO;
        }
        return vaccine.getPriceSale().subtract(vaccine.getPurchasePrice());
    }

    public Page<VaccineResponseDTO> filterAllVaccinesByFilter(String lotNumber, String supplierName, VaccineType vaccineType, BigDecimal minPurchasePrice,
                                                              BigDecimal maxPurchasePrice, BigDecimal minPriceSale, BigDecimal maxPriceSale,
                                                              Integer minStockQuantity, Integer maxStockQuantity, LocalDate manufactureAfter,
                                                              LocalDate expirationBefore, StatusEntity status, Pageable pageable){

        List<VaccineResponseDTO> listVaccines = vaccineRepository.findAll(VaccineSpecification.specification(lotNumber, supplierName, vaccineType, minPurchasePrice,
                maxPurchasePrice, minPriceSale, maxPriceSale, minStockQuantity, maxStockQuantity, manufactureAfter, expirationBefore, status
        ), pageable).map(mapper::toDTO).toList();

        return new PageImpl<>(listVaccines, pageable, listVaccines.size());
    }

    public Vaccine updateVaccineById(UUID vaccineId, VaccineUpdateDTO dto){
        Vaccine vaccine = findById(vaccineId);
        vaccine = validator.validate(vaccine, dto);
        return vaccineRepository.save(vaccine);
    }

}
