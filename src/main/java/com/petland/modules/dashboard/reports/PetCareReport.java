package com.petland.modules.dashboard.reports;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.utils.PetCareServiceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PetCareReport {

    private final PetCareServiceCalculator calculator;

    public Report generate(List<PetCare> petCareList){
        BigDecimal totaRevenue = calculator.calculateTotalRevenueByPetCareList(petCareList);
        BigDecimal totalProfit = calculator.calculateTotalProfitByPetCareList(petCareList);
        BigDecimal totalCostOperating = calculator.calculateTotalCostOperatingByPetCareList(petCareList);
        Integer totalItems = calculator.sumAllItemsSold(petCareList);

        return Report.builder()
                .totalRevenue(totaRevenue)
                .totalProfit(totalProfit)
                .operatingCost(totalCostOperating)
                .itemsQuantity(totalItems)
                .build();
    }
}
