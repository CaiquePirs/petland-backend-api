package com.petland.modules.pet.dto;

import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;

import java.time.LocalDate;

public record PetUpdateDTO(
        String name,
        LocalDate dateBirth,
        PetSpecies specie,
        PetGender gender,
        String breed,
        Integer age,
        Double weight) {
}
