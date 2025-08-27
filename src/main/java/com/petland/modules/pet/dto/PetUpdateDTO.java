package com.petland.modules.pet.dto;

import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PetUpdateDTO(
        String name,
        LocalDate dateBirth,
        PetSpecies specie,
        PetGender gender,
        String breed,
        Integer age,
        Double weight) {
}
