package com.petland.modules.petCare.service;

import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.mappers.PetCareDetailsMapper;
import com.petland.modules.petCare.model.PetCareDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCareDetailsService {

    private final PetCareCalculator calculator;
    private final PetCareDetailsMapper mapper;

    public List<PetCareDetails> createService(List<PetCareDetailsRequestDTO> dtoList){
        return dtoList.stream()
                .map(dto -> {
                    BigDecimal totalRevenue = calculator.calculateTotalRevenue(dto.quantityService(), dto.unitPrice());
                    BigDecimal totalProfit = calculator.calculateTotalProfit(totalRevenue, dto.operatingCost());
                    PetCareDetails petCareDetails = mapper.toEntity(dto);
                    petCareDetails.setTotalByService(totalRevenue);
                    petCareDetails.setProfitByService(totalProfit);
                    return petCareDetails;
                }).toList();
    }
}
