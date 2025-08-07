package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.PetCareReport;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.service.PetCareDetailsService;
import com.petland.modules.petCare.service.PetCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCareReportsService {

    private final PetCareService service;
    private final PetCareDetailsService petCareDetailsService;
    private final PetCareReport report;

    public Report totalByPeriod(LocalDate dateMin, LocalDate dateMax){
        List<PetCare> petCareServices = service.findAllByPeriod(dateMin, dateMax);

        if(petCareServices.isEmpty()){
            throw new NotFoundException("petcare service reports not found");
        }
        return report.generateByPetCare(petCareServices);
    }

    public Report totalByServiceType(String petCareType){
        List<PetCareDetails> petCareDetails = petCareDetailsService.findAllByServiceType(PetCareType.valueOf(petCareType));

        if(petCareDetails.isEmpty()){
            throw new NotFoundException("petcare service reports not found");
        }
        return report.generateByPetCareDetails(petCareDetails);
    }
}
