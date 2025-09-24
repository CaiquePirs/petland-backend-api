package com.petland.modules.pet.controller.dto;

import com.petland.modules.pet.model.enums.PetGender;
import com.petland.modules.pet.model.enums.PetSpecies;

import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO used for complete pet response")
public record PetResponseDTO(
        @Schema(description = "Unique ID of the pet", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Name of the pet", example = "Buddy")
        String name,

        @Schema(description = "Age of the pet in years", example = "3")
        int age,

        @Schema(description = "Pet's date of birth", example = "2020-05-12")
        LocalDate dateBirth,

        @Schema(description = "Pet's species", example = "DOG")
        PetSpecies specie,

        @Schema(description = "Pet's gender", example = "MALE")
        PetGender gender,

        @Schema(description = "Breed of the pet", example = "Labrador")
        String breed,

        @Schema(description = "Weight of the pet in kilograms", example = "15.5")
        double weight,

        @Schema(description = "ID of the customer who owns the pet", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID ownerId
) {}
