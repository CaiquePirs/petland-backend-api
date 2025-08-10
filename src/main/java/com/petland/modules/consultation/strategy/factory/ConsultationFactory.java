package com.petland.modules.consultation.strategy.factory;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.strategy.ConsultationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsultationFactory {

    private final List<ConsultationStrategy> strategies;

    public Consultation execute(Consultation consultation, ConsultationRequestDTO requestDTO){
       for(ConsultationStrategy strategy : strategies){
           consultation = strategy.execute(consultation, requestDTO);
       }
       return consultation;
    }

}
