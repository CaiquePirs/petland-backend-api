package com.petland.modules.pet.controller.dto;

import com.petland.modules.pet.model.enums.PetGender;
import com.petland.modules.pet.model.enums.PetSpecies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Schema(description = "DTO used to create a new pet")
public record PetRequestDTO(

        @NotBlank(message = "Name is required")
        @Schema(description = "Name of the pet", example = "Buddy")
        String name,

        @NotNull(message = "Age is required")
        @Schema(description = "Age of the pet in years", example = "3")
        int age,

        @NotNull(message = "Date birth is required")
        @Schema(description = "Pet's date of birth", example = "2020-05-12")
        LocalDate dateBirth,

        @NotNull(message = "Specie is required")
        @Schema(description = "Pet's species", example = "DOG")
        PetSpecies specie,

        @NotNull(message = "Gender is required")
        @Schema(description = "Pet's gender", example = "MALE")
        PetGender gender,

        @NotBlank(message = "Breed is required")
        @Schema(description = "Breed of the pet", example = "Labrador")
        String breed,

        @NotNull(message = "Weight is required")
        @Schema(description = "Weight of the pet in kilograms", example = "15.5")
        double weight,

        @NotNull(message = "Customer ID is required")
        @Schema(description = "ID of the customer who owns the pet", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID customerId
) {}

