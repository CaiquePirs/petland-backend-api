package com.petland.modules.petCare.mappers;

import com.petland.modules.petCare.dtos.BathRequestDTO;
import com.petland.modules.petCare.dtos.BathResponseDTO;
import com.petland.modules.petCare.model.Bath;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BathMapper {

    Bath toEntity(BathRequestDTO bathRequestDTO);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "employee.id", target = "employeeId")
    BathResponseDTO toDTO(Bath bath);
}
