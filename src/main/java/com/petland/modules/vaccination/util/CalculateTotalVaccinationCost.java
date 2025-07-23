package com.petland.modules.vaccination.util;

import com.petland.modules.vaccination.module.AppliedVaccine;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalculateTotalVaccinationCost {

    public BigDecimal calculate(List<AppliedVaccine> listAppliedVaccine){
        BigDecimal totalVaccination = BigDecimal.ZERO;

        for (AppliedVaccine appliedVaccine : listAppliedVaccine) {
            BigDecimal quantity = BigDecimal.valueOf(appliedVaccine.getQuantityUsed());
            BigDecimal price = appliedVaccine.getVaccine().getPriceSale();

            BigDecimal total = quantity.multiply(price);
            totalVaccination = totalVaccination.add(total);
        }
        return totalVaccination;
    }
}
