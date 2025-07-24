package com.petland.modules.petCare.mappers;

import com.petland.modules.petCare.dtos.GroomingRequestDTO;
import com.petland.modules.petCare.dtos.GroomingResponseDTO;
import com.petland.modules.petCare.model.Grooming;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroomingMapper {

    Grooming toEntity(GroomingRequestDTO groomingRequestDTO);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "employee.id", target = "employeeId")
    GroomingResponseDTO toDTO(Grooming grooming);
}
