package com.petland.modules.vaccination.calculator;

import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VaccinationCalculator {

    private BigDecimal calculateProfitByVaccine(Vaccine vaccine){
        if(vaccine.getPurchasePrice() == null || vaccine.getPriceSale() == null) return BigDecimal.ZERO;
        return vaccine.getPriceSale().subtract(vaccine.getPurchasePrice());
    }

    public BigDecimal calculateTotalVaccination(List<AppliedVaccine> listAppliedVaccine) {
        return listAppliedVaccine
                .stream()
                .map(a -> a.getVaccine().getPriceSale().multiply(BigDecimal.valueOf(a.getQuantityUsed())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateProfitByVaccineApplied(List<AppliedVaccine> listAppliedVaccine){
        return listAppliedVaccine
                .stream()
                .map(a -> calculateProfitByVaccine(a.getVaccine()).multiply(BigDecimal.valueOf(a.getQuantityUsed())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
     return vaccinations
             .stream()
             .flatMap(vaccination -> vaccination.getAppliedVaccines().stream())
             .map(a -> a.getVaccine().getPurchasePrice().multiply(BigDecimal.valueOf(a.getQuantityUsed())))
             .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
