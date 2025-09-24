package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.model.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCareReportsService {

    private final PetCareService service;
    private final PetCareCalculator calculator;
    private final BuilderReport builder;

    public Report totalByPeriod(LocalDate dateMin, LocalDate dateMax){
        List<PetCare> petCareServices = service.findAllByPeriod(dateMin, dateMax);
        return generate(petCareServices);
    }

    public Report totalByServiceType(PetCareType petCareType){
        List<PetCare> petCareServices = service.findAllByPetCareType(petCareType);
        return generate(petCareServices);
    }

    private Report generate(List<PetCare> petCareList){
        validateIfListPetCareIsEmpty(petCareList);
        BigDecimal totaRevenue = calculator.calculateTotalRevenueByPetCareList(petCareList);
        BigDecimal totalProfit = calculator.calculateTotalProfitByPetCareList(petCareList);
        BigDecimal totalCostOperating = calculator.calculateTotalCostOperatingByPetCareList(petCareList);
        Integer totalItems = calculator.sumAllItemsSold(petCareList);
        return builder.generate(totaRevenue, totalProfit, totalItems, totalCostOperating);
    }

    private void validateIfListPetCareIsEmpty(List<PetCare> petCares){
        if(petCares.isEmpty()){
            throw new NotFoundException("PetCare list reports not found");
        }
    }

}
