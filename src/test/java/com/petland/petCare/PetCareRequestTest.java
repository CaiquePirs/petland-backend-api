package com.petland.petCare;

import com.petland.common.entity.Address;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PetCareRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> provideInvalidFields() {
        List<PetCareDetailsRequestDTO> petCareDetailsRequest = new ArrayList<>();

        return Stream.of(
                Arguments.of(null, UUID.randomUUID(), UUID.randomUUID(), List.of(petCareDetailsRequest), new Address(), "Pet ID is required"),
                Arguments.of(UUID.randomUUID(), null, UUID.randomUUID(), List.of(petCareDetailsRequest), new Address(), "Customer ID is required"),
                Arguments.of(UUID.randomUUID(), UUID.randomUUID(), null, List.of(petCareDetailsRequest), new Address(), "Employee ID is required"),
                Arguments.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, new Address(), "Services details is required"),
                Arguments.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(petCareDetailsRequest), null, "Location is required")
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected: {5}")
    @MethodSource("provideInvalidFields")
    void shouldFailValidationWhenFieldIsNull(
            UUID petId,
            UUID customerId,
            UUID employeeId,
            List<PetCareDetailsRequestDTO> serviceDetailsList,
            Address location,
            String expectedMessage
    ) {
        PetCareRequestDTO dto = PetCareRequestDTO.builder()
                .petId(petId)
                .customerId(customerId)
                .employeeId(employeeId)
                .serviceDetailsList(serviceDetailsList)
                .location(location)
                .build();

        Set<ConstraintViolation<PetCareRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    void shouldPassValidationWhenAllFieldsValid() {
        List<PetCareDetailsRequestDTO> petCareDetailsRequest = new ArrayList<>();
        PetCareRequestDTO dto = PetCareRequestDTO.builder()
                .petId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .employeeId(UUID.randomUUID())
                .serviceDetailsList(petCareDetailsRequest)
                .location(new Address())
                .build();

        Set<ConstraintViolation<PetCareRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "DTO should be valid when all fields are provided");
    }
}
