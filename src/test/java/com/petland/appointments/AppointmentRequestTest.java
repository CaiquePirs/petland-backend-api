package com.petland.appointments;

import com.petland.modules.appointment.controller.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.model.enums.ServiceType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointmentRequestTest {

    private Validator validator;
    private UUID customerId;
    private UUID petId;
    private LocalDate validDate;
    private LocalTime validHour;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        customerId = UUID.randomUUID();
        petId = UUID.randomUUID();
        validDate = LocalDate.now().plusDays(5);
        validHour = LocalTime.now().plusHours(5);
    }

    private Stream<Arguments> provideInvalidAppointments() {
        return Stream.of(
                Arguments.of(null, petId, validDate, validHour, ServiceType.GROOMING, AppointmentStatus.SCHEDULED, "Customer ID is required"),
                Arguments.of(customerId, null, validDate, validHour, ServiceType.GROOMING, AppointmentStatus.SCHEDULED, "Pet ID is required"),
                Arguments.of(customerId, petId, null, validHour, ServiceType.GROOMING, AppointmentStatus.SCHEDULED, "Appointment date is required"),
                Arguments.of(customerId, petId, LocalDate.now().minusDays(1), validHour, ServiceType.GROOMING, AppointmentStatus.SCHEDULED, "Appointment date must be in the future"),
                Arguments.of(customerId, petId, validDate, null, ServiceType.GROOMING, AppointmentStatus.SCHEDULED, "Appointment hour is required"),
                Arguments.of(customerId, petId, validDate, validHour, null, AppointmentStatus.SCHEDULED, "Appointment type is required")
        );
    }

    @ParameterizedTest(name = "Invalid field test #{index} - expecting: {6}")
    @MethodSource("provideInvalidAppointments")
    void shouldRefuseSchedulingWithInvalidFields(UUID customerId, UUID petId, LocalDate appointmentDate,
                                                 LocalTime appointmentHour, ServiceType appointmentType,
                                                 AppointmentStatus appointmentStatus, String expectedMessage) {

        AppointmentRequestDTO dto = new AppointmentRequestDTO(
                customerId, petId, appointmentDate,
                appointmentHour, appointmentType, appointmentStatus
        );
        Set<ConstraintViolation<AppointmentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)),
                "Expected to find message: " + expectedMessage);
    }

    @Test
    void mustAcceptAppointmentWithValidFields() {
        AppointmentRequestDTO dto = new AppointmentRequestDTO(
                customerId, petId,
                validDate, validHour,
                ServiceType.GROOMING, AppointmentStatus.SCHEDULED
        );
        assertDoesNotThrow(() -> validator.validate(dto));
        assertAll(
                () -> assertEquals(customerId, dto.customerId()),
                () -> assertEquals(petId, dto.petId()),
                () -> assertEquals(validDate, dto.appointmentDate()),
                () -> assertEquals(validHour, dto.appointmentHour()),
                () -> assertEquals(ServiceType.GROOMING, dto.appointmentType()),
                () -> assertEquals(AppointmentStatus.SCHEDULED, dto.appointmentStatus())
        );
    }
}

