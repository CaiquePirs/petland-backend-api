package com.petland.modules.consultation.mappers;

import com.petland.modules.consultation.controller.dtos.ConsultationDetailsHistoryDTO;
import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.model.ConsultationDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    Consultation toEntity(ConsultationRequestDTO requestDTO);
    ConsultationDetailsHistoryDTO toDetailsDTO(ConsultationDetails details);
}
