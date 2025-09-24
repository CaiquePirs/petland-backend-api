package com.petland.modules.consultation.strategy.impl;

import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.strategy.ConsultationStrategy;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetCareConsultationImpl implements ConsultationStrategy {

    private final PetCareService petCareService;

    public Consultation execute(Consultation consultation, ConsultationRequestDTO requestDTO) {
        if(requestDTO.petCareRequestDTO() != null){
            PetCare petCare = petCareService.register(requestDTO.petCareRequestDTO());
            consultation.setService(petCare);
        }
        return consultation;
    }
}
