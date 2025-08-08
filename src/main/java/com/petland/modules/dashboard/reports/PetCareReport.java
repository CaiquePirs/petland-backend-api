package com.petland.modules.dashboard.reports;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PetCareReport {

    private final PetCareCalculator calculator;

    public Report generateByPetCare(List<PetCare> petCareList){
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

    public Report generateByPetCareDetails(List<PetCareDetails> petCareDetails){
        BigDecimal totaRevenue = calculator.calculateTotalRevenueByServiceList(petCareDetails);
        BigDecimal totalProfit = calculator.calculateTotalProfitByServiceList(petCareDetails);
        BigDecimal totalCostOperating = calculator.calculateTotalCostOperatingByServiceList(petCareDetails);
        Integer totalItems = calculator.sumAllItemsSoldByServiceList(petCareDetails);

        return Report.builder()
                .totalRevenue(totaRevenue)
                .totalProfit(totalProfit)
                .operatingCost(totalCostOperating)
                .itemsQuantity(totalItems)
                .build();
    }
}
