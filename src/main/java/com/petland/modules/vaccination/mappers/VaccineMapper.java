package com.petland.modules.vaccination.mappers;

import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.module.Vaccine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccineMapper {

    Vaccine toEntity(VaccineRequestDTO vaccineRequestDTO);
    VaccineResponseDTO toDTO(Vaccine vaccine);
}
