package com.petland.vaccination;

import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AppliedVaccineRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> invalidAppliedVaccineProvider() {
        return Stream.of(
                Arguments.of(
                        new AppliedVaccineRequestDTO(null, 1),
                        "Vaccine ID is required"
                )
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected={1}")
    @MethodSource("invalidAppliedVaccineProvider")
    void shouldFailValidationWhenRequiredFieldsAreNull(AppliedVaccineRequestDTO dto, String expectedMessage) {
        Set<ConstraintViolation<AppliedVaccineRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals(expectedMessage));
    }

    @Test
    void shouldPassValidationHappyPath() {
        AppliedVaccineRequestDTO dto = new AppliedVaccineRequestDTO(
                UUID.randomUUID(),
                2
        );

        Set<ConstraintViolation<AppliedVaccineRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}

