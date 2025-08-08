package com.petland.modules.vaccination.calculator;

import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
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

    public BigDecimal calculateTotalBilledByVaccinationsList(List<Vaccination> vaccinationList){
        return vaccinationList
                .stream()
                .map(Vaccination::getTotalByVaccination)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalProfitByVaccinationsList(List<Vaccination> vaccinationList){
        return vaccinationList
                .stream()
                .map(Vaccination::getProfitByVaccination)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Integer sumTotalVaccinationsApplied(List<Vaccination> vaccinationList){
        return vaccinationList
                .stream()
                .mapToInt(vaccination -> vaccination.getAppliedVaccines().size())
                .sum();
    }

    public BigDecimal calculateTotalCostVaccine(List<Vaccination> vaccinations) {
     return vaccinations.stream()
             .flatMap(vaccination -> vaccination.getAppliedVaccines().stream())
             .map(a -> a.getVaccine().getPurchasePrice().multiply(BigDecimal.valueOf(a.getQuantityUsed())))
             .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
