package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.PetCareReport;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCareReportsService {

    private final PetCareService service;
    private final PetCareReport report;

    public Report totalServicesByPeriod(LocalDate dateMin, LocalDate dateMax){
        List<PetCare> petCareServices = service.findAllByPeriod(dateMin, dateMax);

        if(petCareServices.isEmpty()){
            throw new NotFoundException("petcare service reports not found");
        }

        return report.generate(petCareServices);
    }
}
