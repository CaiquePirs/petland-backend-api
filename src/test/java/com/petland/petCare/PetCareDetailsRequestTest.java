package com.petland.petCare;

import com.petland.modules.petCare.controller.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.model.enums.PetCareType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PetCareDetailsRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> provideInvalidFields() {
        return Stream.of(
                Arguments.of(null, BigDecimal.valueOf(10), 1, BigDecimal.valueOf(5), "Valid note", "Service type is required"),
                Arguments.of(PetCareType.BATH, null, 1, BigDecimal.valueOf(5), "Valid note", "Unit price is required."),
                Arguments.of(PetCareType.BATH, BigDecimal.ZERO, 1, BigDecimal.valueOf(5), "Valid note", "Unit price must be greater than zero"),
                Arguments.of(PetCareType.BATH, BigDecimal.valueOf(10), 0, BigDecimal.valueOf(5), "Valid note", "Quantity must be at least 1"),
                Arguments.of(PetCareType.BATH, BigDecimal.valueOf(10), 1, null, "Valid note", "Operating cost is required."),
                Arguments.of(PetCareType.BATH, BigDecimal.valueOf(10), 1, BigDecimal.valueOf(-1), "Valid note", "Operating cost must be zero or greater"),
                Arguments.of(PetCareType.BATH, BigDecimal.valueOf(10), 1, BigDecimal.valueOf(5), "a".repeat(501), "Service notes must be at most 500 characters")
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected: {5}")
    @MethodSource("provideInvalidFields")
    void shouldFailValidationWhenInvalid(
            PetCareType petCareType,
            BigDecimal unitPrice,
            int quantityService,
            BigDecimal operatingCost,
            String noteService,
            String expectedMessage
    ) {
        PetCareDetailsRequestDTO dto = new PetCareDetailsRequestDTO(
                petCareType,
                unitPrice,
                quantityService,
                operatingCost,
                noteService
        );

        Set<ConstraintViolation<PetCareDetailsRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    void shouldPassValidationWhenAllFieldsValid() {
        PetCareDetailsRequestDTO dto = new PetCareDetailsRequestDTO(
                PetCareType.BATH,
                BigDecimal.valueOf(25.50),
                2,
                BigDecimal.valueOf(10),
                "Dog grooming service"
        );

        Set<ConstraintViolation<PetCareDetailsRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "DTO should be valid when all fields are correct");
    }
}
