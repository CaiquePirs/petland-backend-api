package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationsReportsService {

    private final VaccinationService service;
    private final VaccinationCalculator calculator;
    private final BuilderReport builderReport;

    public Report totalByPeriod(LocalDate dateMin, LocalDate dateMax) {
        List<Vaccination> vaccinationsList = service.findAllVaccinationsByPeriod(dateMin, dateMax);
        return generate(vaccinationsList);
    }

    public Report totalByVaccine(UUID vaccineId) {
        List<Vaccination> vaccinationsList = service.findAllVaccinationsByVaccine(vaccineId);
        return generate(vaccinationsList);
    }

    private Report generate(List<Vaccination> vaccinationsList) {
        validateIfListVaccinationIsEmpty(vaccinationsList);
        BigDecimal totalBilled = calculator.calculateTotalBilledByVaccinationsList(vaccinationsList);
        BigDecimal totalProfit = calculator.calculateTotalProfitByVaccinationsList(vaccinationsList);
        BigDecimal costOperating = calculator.calculateTotalCostVaccine(vaccinationsList);
        Integer sumTotalVaccinations = calculator.sumTotalVaccinationsApplied(vaccinationsList);
        return builderReport.generate(totalBilled, totalProfit, sumTotalVaccinations, costOperating);
    }

    private void validateIfListVaccinationIsEmpty(List<Vaccination> vaccinations){
        if(vaccinations.isEmpty()){
            throw new NotFoundException("Vaccination list reports not found");
        }
    }

}
