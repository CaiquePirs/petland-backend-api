package com.petland.modules.pet.dto;

import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PetRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Age is required")
        int age,

        @NotNull(message = "Date birth is required")
        LocalDate dateBirth,

        @NotNull(message = "Specie is required")
        PetSpecies specie,

        @NotNull(message = "Gender is required")
        PetGender gender,

        @NotBlank(message = "Breed is required")
        String breed,

        @NotNull(message = "Weight is required")
        double weight,

        @NotNull(message = "Customer ID is required")
        UUID customerId) {
}
