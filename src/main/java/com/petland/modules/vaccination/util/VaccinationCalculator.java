package com.petland.modules.vaccination.util;

import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.VaccineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VaccinationCalculator {

    private final VaccineService vaccineService;

    public BigDecimal calculateTotalVaccination(List<AppliedVaccine> listAppliedVaccine){
        BigDecimal totalVaccination = BigDecimal.ZERO;

        for (AppliedVaccine appliedVaccine : listAppliedVaccine) {
            BigDecimal quantity = BigDecimal.valueOf(appliedVaccine.getQuantityUsed());
            BigDecimal price = appliedVaccine.getVaccine().getPriceSale();
            BigDecimal total = price.multiply(quantity);
            totalVaccination = totalVaccination.add(total);
        }
        return totalVaccination;
    }

    public BigDecimal calculateProfitByVaccineApplied(List<AppliedVaccine> listAppliedVaccine){
        BigDecimal totalProfit = BigDecimal.ZERO;

        for(AppliedVaccine appliedVaccine : listAppliedVaccine){
            Vaccine vaccine = appliedVaccine.getVaccine();
            BigDecimal profitVaccine = vaccineService.calculateProfitByVaccine(vaccine);
            totalProfit = profitVaccine.multiply(BigDecimal.valueOf(appliedVaccine.getQuantityUsed()));
        }
        return totalProfit;
    }
}
