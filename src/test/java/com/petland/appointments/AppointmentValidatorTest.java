package com.petland.appointments;

import com.petland.common.exception.InvalidAppointmentException;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.validator.AppointmentValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentValidatorTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentValidator validator;

    @Test
    void shouldRefuseAppointmentWithDateGreaterThan30Days() {
        Assertions.assertThrows(InvalidAppointmentException.class, () -> {
            validator.validateAppointmentTimeWindow(LocalDate.now().plusDays(31), LocalTime.now());
        });
    }

    @Test
    void mustAcceptAppointmentsWithDateLessOrEqualsThan30Days() {
        Assertions.assertDoesNotThrow(() -> {
            validator.validateAppointmentTimeWindow(LocalDate.now().plusDays(30), LocalTime.now());
        });
    }

    @Test
    void mustRefuseAppointmentsWithLessThan10HoursInAdvance() {
        LocalDateTime appointmentDate = LocalDateTime.now().plusHours(10);
        Assertions.assertThrows(InvalidAppointmentException.class, () -> {
            validator.validateAppointmentTimeWindow(appointmentDate.toLocalDate(), appointmentDate.toLocalTime());
        });
    }

    @Test
    void mustAcceptAppointmentsWithTimesLongerThan10Hours() {
        LocalDateTime appointmentDate = LocalDateTime.now().plusHours(11);
        Assertions.assertDoesNotThrow(() -> {
            validator.validateAppointmentTimeWindow(appointmentDate.toLocalDate(), appointmentDate.toLocalTime());
        });
    }

    @Test
    void shouldThrowExceptionWhenAppointmentExistsAtSameTime() {
        LocalDate date = LocalDate.of(2025, 8, 20);
        LocalTime hour = LocalTime.of(14, 30);

        when(repository.findOne(any(Specification.class))).thenReturn(Optional.of(new Appointment()));
        InvalidAppointmentException ex = assertThrows(
                InvalidAppointmentException.class,
                () -> validator.checkForExistingAppointmentAtSameTime(date, hour)
        );

        assertEquals("There is already an appointment with this time", ex.getMessage());
        verify(repository).findOne(any(Specification.class));
    }

    @Test
    void shouldNotThrowExceptionWhenNoAppointmentExists(){
        LocalDate date = LocalDate.of(2025, 8, 20);
        LocalTime time = LocalTime.of(14, 30);

        when(repository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> validator.checkForExistingAppointmentAtSameTime(date, time));
        verify(repository).findOne(any(Specification.class));
    }
}
