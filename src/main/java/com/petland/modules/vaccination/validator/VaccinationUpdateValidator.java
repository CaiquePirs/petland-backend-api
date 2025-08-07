package com.petland.modules.vaccination.validator;

import com.petland.common.entity.Address;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.common.auth.validator.AddressUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccinationUpdateValidator {

    private final AddressUpdateValidator addressValidator;

    public Vaccination validate(Vaccination vaccination, VaccinationUpdateDTO dto){
        if(dto.location() != null){
            Address addressUpdated = addressValidator.validate(dto.location(), vaccination.getLocation());
            vaccination.setLocation(addressUpdated);
        }

        if(dto.clinicalNotes() != null){
            vaccination.setClinicalNotes(dto.clinicalNotes());
        }

        if(dto.vaccinationDate() != null){
            vaccination.setVaccinationDate(dto.vaccinationDate());
        }

        if(dto.nextDoseDate() != null){
            vaccination.setNextDoseDate(dto.nextDoseDate());
        }

        return vaccination;
    }
}
