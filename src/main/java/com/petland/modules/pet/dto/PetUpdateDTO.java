package com.petland.modules.pet.dto;

import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update an existing pet. All fields are optional.")
public record PetUpdateDTO(
        @Schema(description = "Name of the pet", example = "Buddy")
        String name,

        @Schema(description = "Pet's date of birth", example = "2020-05-12")
        LocalDate dateBirth,

        @Schema(description = "Pet's species", example = "DOG")
        PetSpecies specie,

        @Schema(description = "Pet's gender", example = "MALE")
        PetGender gender,

        @Schema(description = "Breed of the pet", example = "Labrador")
        String breed,

        @Schema(description = "Age of the pet in years", example = "3")
        Integer age,

        @Schema(description = "Weight of the pet in kilograms", example = "15.5")
        Double weight
) {}
