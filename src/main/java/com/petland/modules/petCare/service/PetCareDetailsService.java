package com.petland.modules.petCare.service;

import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.mappers.PetCareDetailsMapper;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.repositories.PetCareDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCareDetailsService {

    private final PetCareCalculator calculator;
    private final PetCareDetailsMapper mapper;
    private final PetCareDetailsRepository repository;

    public List<PetCareDetails> createService(PetCare petCare, List<PetCareDetailsRequestDTO> dtoList){
        return dtoList.stream()
                .map(dto -> {
                    BigDecimal totalRevenue = calculator.calculateTotalRevenue(dto.quantityService(), dto.unitPrice());
                    BigDecimal totalProfit = calculator.calculateTotalProfit(totalRevenue, dto.operatingCost());
                    PetCareDetails petCareDetails = mapper.toEntity(dto);
                    petCareDetails.setTotalByService(totalRevenue);
                    petCareDetails.setProfitByService(totalProfit);
                    petCareDetails.setPetCare(petCare);
                    return petCareDetails;
                }).toList();
    }

    public List<PetCareDetails> findAllByServiceType(PetCareType petCareType) {
        return repository.findByPetCareType(petCareType);
    }
}
