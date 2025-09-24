package com.petland.modules.pet.mapper;

import com.petland.modules.pet.controller.dto.PetRequestDTO;
import com.petland.modules.pet.controller.dto.PetResponseDTO;
import com.petland.modules.pet.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    Pet toEntity(PetRequestDTO petRequestDTO);

    @Mapping(source = "owner.id", target = "ownerId")
    PetResponseDTO toDTO(Pet pet);
}
