package com.petland.modules.vaccination.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;

import java.time.LocalDate;

public record VaccinationUpdateDTO(UpdateAddressDTO location,
                                   String clinicalNotes,
                                   LocalDate vaccinationDate,
                                   LocalDate nextDoseDate) {
}
