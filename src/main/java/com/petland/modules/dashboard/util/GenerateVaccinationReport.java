package com.petland.modules.dashboard.util;

import com.petland.modules.dashboard.dtos.ReportsResponseDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.util.VaccinationCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateVaccinationReport {

    private final VaccinationCalculator calculator;

    public ReportsResponseDTO generate(List<Vaccination> vaccinationList){
        BigDecimal totalBilled = calculator.calculateTotalBilledByVaccinationsList(vaccinationList);
        BigDecimal totalProfit = calculator.calculateTotalProfitByVaccinationsList(vaccinationList);
        Integer sumTotalVaccinations = calculator.sumTotalVaccinationsApplied(vaccinationList);

        return ReportsResponseDTO.builder()
                .totalSales(totalBilled)
                .profitMargin(totalProfit)
                .itemsQuantity(sumTotalVaccinations)
                .build();
    }

}
