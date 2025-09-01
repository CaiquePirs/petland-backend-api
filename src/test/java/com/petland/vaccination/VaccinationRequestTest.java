package com.petland.vaccination;

import com.petland.common.entity.Address;
import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class VaccinationRequestTest {


    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> invalidVaccinationRequestProvider() {
        return Stream.of(
                Arguments.of(new VaccinationRequestDTO(null, UUID.randomUUID(), UUID.randomUUID(),
                                List.of(AppliedVaccineRequestDTO.builder().build()), LocalDate.now(), null,
                                new Address(), "notes"), "Pet ID is required"
                ),
                Arguments.of(
                        new VaccinationRequestDTO(UUID.randomUUID(), null, UUID.randomUUID(),
                                List.of(AppliedVaccineRequestDTO.builder().build()), LocalDate.now(), null,
                                new Address(), "notes"), "Owner Id is required"
                ),
                Arguments.of(
                        new VaccinationRequestDTO(UUID.randomUUID(), UUID.randomUUID(), null,
                                List.of(AppliedVaccineRequestDTO.builder().build()), LocalDate.now(), null,
                                new Address(), "notes"), "Veterinarian ID is required"
                ),
                Arguments.of(
                        new VaccinationRequestDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                                null, LocalDate.now(), null, new Address(),
                                "notes"), "Vaccine applied is required"
                ),
                Arguments.of(new VaccinationRequestDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                                List.of(AppliedVaccineRequestDTO.builder().build()), null,
                                null, new Address(), "notes"), "Vaccination date is required"
                ),
                Arguments.of(
                        new VaccinationRequestDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                                List.of(AppliedVaccineRequestDTO.builder().build()), LocalDate.now(), null,
                                null, "notes"), "Vaccination location is required"
                )
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected={1}")
    @MethodSource("invalidVaccinationRequestProvider")
    void shouldFailValidationWhenRequiredFieldsAreNull(VaccinationRequestDTO dto, String expectedMessage) {
        Set<ConstraintViolation<VaccinationRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals(expectedMessage));
    }

    @Test
    void shouldPassValidationHappyPath() {
        VaccinationRequestDTO dto = new VaccinationRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                List.of(AppliedVaccineRequestDTO.builder().build()),
                LocalDate.now(), LocalDate.now().plusDays(30),
                new Address(), "Notes"
        );
        Set<ConstraintViolation<VaccinationRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
