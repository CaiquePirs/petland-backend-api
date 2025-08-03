package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.VaccinationReport;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationsReportsService {

    private final VaccinationService vaccinationService;
    private final VaccinationReport report;

    public Report totalBilledByPeriod(LocalDate dateMin, LocalDate dateMax){
        List<Vaccination> vaccinationsList = vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax);

        if(vaccinationsList.isEmpty()){
            throw new NotFoundException("Vaccinations report not found");
        }
        return report.generate(vaccinationsList);
    }
    public Report totalBilledByVaccine(UUID vaccineId){
        List<Vaccination> vaccinationsList = vaccinationService.findAllVaccinationsByVaccine(vaccineId);

        if(vaccinationsList.isEmpty()){
            throw new NotFoundException("Vaccinations report not found");
        }
        return report.generate(vaccinationsList);
    }

}
