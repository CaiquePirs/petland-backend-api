package com.petland.modules.dashboard.reports;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.util.VaccinationCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VaccinationReport {

    private final VaccinationCalculator calculator;

    public Report generate(List<Vaccination> vaccinationList){
        BigDecimal totalBilled = calculator.calculateTotalBilledByVaccinationsList(vaccinationList);
        BigDecimal totalProfit = calculator.calculateTotalProfitByVaccinationsList(vaccinationList);
        Integer sumTotalVaccinations = calculator.sumTotalVaccinationsApplied(vaccinationList);

        return Report.builder()
                .totalSales(totalBilled)
                .profitMargin(totalProfit)
                .itemsQuantity(sumTotalVaccinations)
                .build();
    }

}
