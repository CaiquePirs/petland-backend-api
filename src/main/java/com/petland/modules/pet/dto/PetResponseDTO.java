package com.petland.modules.pet.dto;

import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponseDTO(
        UUID id,
        String name,
        int age,
        LocalDate dateBirth,
        PetSpecies specie,
        PetGender gender,
        String breed,
        double weight,
        UUID ownerId
) {
}
