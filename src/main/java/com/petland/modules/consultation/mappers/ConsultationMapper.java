package com.petland.modules.consultation.mappers;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    Consultation toEntity(ConsultationRequestDTO requestDTO);
}
