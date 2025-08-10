package com.petland.modules.consultation.strategy;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;

public interface ConsultationStrategy {
    Consultation execute(Consultation consultation, ConsultationRequestDTO requestDTO);
}
