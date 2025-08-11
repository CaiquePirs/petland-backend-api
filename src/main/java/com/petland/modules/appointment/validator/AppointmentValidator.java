package com.petland.modules.appointment.validator;

import com.petland.common.exception.InvalidAppointmentTimeException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AppointmentValidator {

    public void validateAppointmentTimeWindow(LocalDate appointmentDate, LocalTime appointmentHour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentHour);

        if (appointmentDateTime.isBefore(now.plusHours(24))) {
            throw new InvalidAppointmentTimeException("Appointment must be scheduled at least 3 hour in advance");
        }
        if (appointmentDateTime.isAfter(now.plusDays(30))) {
            throw new InvalidAppointmentTimeException("Appointment cannot be scheduled more than 30 days in advance");
        }
    }
}
