package com.petland.modules.consultation.strategy.impl;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.strategy.ConsultationStrategy;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccinationConsultationStrategy implements ConsultationStrategy {

    private final VaccinationService service;

    public Consultation execute(Consultation consultation, ConsultationRequestDTO requestDTO) {
        if(requestDTO.vaccinationRequestDTO() != null){
            Vaccination vaccination = service.register(requestDTO.vaccinationRequestDTO());
            consultation.setVaccination(vaccination);
        }
        return consultation;
    }
}
