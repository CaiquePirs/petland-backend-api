package com.petland.modules.vaccination.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VaccinationUpdateDTO(UpdateAddressDTO location,
                                   String clinicalNotes,
                                   LocalDate vaccinationDate,
                                   LocalDate nextDoseDate) {
}
