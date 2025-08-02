package com.petland.modules.petCare.mappers;

import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.dtos.PetCareDetailsResponseDTO;
import com.petland.modules.petCare.model.PetCareDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetCareDetailsMapper {

    PetCareDetails toEntity(PetCareDetailsRequestDTO dto);

    @Mapping(source = "petCare.id", target = "petCareId")
    PetCareDetailsResponseDTO toDTO(PetCareDetails petCareDetails);
}
