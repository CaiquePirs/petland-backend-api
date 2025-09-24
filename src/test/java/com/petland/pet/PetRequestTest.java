package com.petland.pet;

import com.petland.modules.pet.controller.dto.PetRequestDTO;
import com.petland.modules.pet.model.enums.PetGender;
import com.petland.modules.pet.model.enums.PetSpecies;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PetRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Arguments> generateInvalidArguments() {
        UUID customerId = UUID.randomUUID();

        return Stream.of(
                Arguments.of(null, 3, LocalDate.of(2020, 5, 10), PetSpecies.DOG, PetGender.MALE, "Golden", 10.0, customerId, "Name is required"),
                Arguments.of("Buddy", 3, null, PetSpecies.DOG, PetGender.MALE, "Golden", 10.0, customerId, "Date birth is required"),
                Arguments.of("Buddy", 3, LocalDate.of(2020, 5, 10), null, PetGender.MALE, "Golden", 10.0, customerId, "Specie is required"),
                Arguments.of("Buddy", 3, LocalDate.of(2020, 5, 10), PetSpecies.DOG, null, "Golden", 10.0, customerId, "Gender is required"),
                Arguments.of("Buddy", 3, LocalDate.of(2020, 5, 10), PetSpecies.DOG, PetGender.MALE, null, 10.0, customerId, "Breed is required"),
                Arguments.of("Buddy", 3, LocalDate.of(2020, 5, 10), PetSpecies.DOG, PetGender.MALE, "Golden", 10.0, null, "Customer ID is required")
        );
    }

    @ParameterizedTest(name = "Test - {index} - Expected = {8}")
    @MethodSource("generateInvalidArguments")
    void shouldValidateInvalidArguments(String name, int age, LocalDate dateBirth,
                                        PetSpecies specie, PetGender gender, String breed,
                                        double weight, UUID customerId, String expectedMessage) {

        PetRequestDTO request = new PetRequestDTO(name, age, dateBirth, specie, gender, breed, weight, customerId);
        Set<ConstraintViolation<PetRequestDTO>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    @DisplayName("Should accept valid PetRequestDTO")
    void shouldAcceptValidRequest() {
        PetRequestDTO request = new PetRequestDTO(
                "Buddy",
                3,
                LocalDate.of(2020, 5, 10),
                PetSpecies.DOG,
                PetGender.MALE,
                "Golden Retriever",
                25.0,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<PetRequestDTO>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());

        assertAll(
                () -> assertEquals("Buddy", request.name()),
                () -> assertEquals(3, request.age()),
                () -> assertEquals(LocalDate.of(2020, 5, 10), request.dateBirth()),
                () -> assertEquals(PetSpecies.DOG, request.specie()),
                () -> assertEquals(PetGender.MALE, request.gender()),
                () -> assertEquals("Golden Retriever", request.breed()),
                () -> assertEquals(25.0, request.weight()),
                () -> assertNotNull(request.customerId())
        );
    }
}
