package com.petland.vaccination;

import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.enums.VaccineType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class VaccineRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> invalidVaccineRequestProvider() {
        return Stream.of(
                Arguments.of(
                        new VaccineRequestDTO(null, "Supplier", VaccineType.RABIES,
                                BigDecimal.TEN, BigDecimal.ONE, 10,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Lot number is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", null, VaccineType.RABIES,
                                BigDecimal.TEN, BigDecimal.ONE, 10,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Supplier name is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", null,
                                BigDecimal.TEN, BigDecimal.ONE, 10,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Vaccine Type is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", VaccineType.RABIES,
                                null, BigDecimal.ONE, 10,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Purchase price is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", VaccineType.RABIES,
                                BigDecimal.TEN, null, 10,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Price sale is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", VaccineType.RABIES,
                                BigDecimal.TEN, BigDecimal.ONE, null,
                                LocalDate.now(), LocalDate.now().plusDays(30)),
                        "Stock Quantity is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", VaccineType.RABIES,
                                BigDecimal.TEN, BigDecimal.ONE, 10,
                                null, LocalDate.now().plusDays(30)),
                        "Manufacture Date is required"
                ),
                Arguments.of(
                        new VaccineRequestDTO("123", "Supplier", VaccineType.RABIES,
                                BigDecimal.TEN, BigDecimal.ONE, 10,
                                LocalDate.now(), null),
                        "expiration Date is required"
                )
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected={1}")
    @MethodSource("invalidVaccineRequestProvider")
    void shouldFailValidationWhenRequiredFieldsAreInvalid(VaccineRequestDTO dto, String expectedMessage) {
        Set<ConstraintViolation<VaccineRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals(expectedMessage));
    }

    @Test
    void shouldPassValidationHappyPath() {
        VaccineRequestDTO dto = new VaccineRequestDTO(
                "L123",
                "SupplierX",
                VaccineType.RABIES,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(80),
                100,
                LocalDate.now(),
                LocalDate.now().plusDays(180)
        );

        Set<ConstraintViolation<VaccineRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
