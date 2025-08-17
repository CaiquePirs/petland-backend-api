package com.petland.modules.appointment.validator;

import com.petland.common.exception.InvalidAppointmentException;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.specifications.AppointmentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

    private final AppointmentRepository repository;

    public void validateAppointmentTimeWindow(LocalDate appointmentDate, LocalTime appointmentHour) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentHour);

        if (appointmentDateTime.isBefore(now.plusHours(10))) {
            throw new InvalidAppointmentException("Appointment must be scheduled at least 10 hour in advance");
        }
        if (appointmentDateTime.isAfter(now.plusDays(30))) {
            throw new InvalidAppointmentException("Appointment cannot be scheduled more than 30 days in advance");
        }
    }

    public void checkForExistingAppointmentAtSameTime(LocalDate appointmentDate, LocalTime appointmentHour){
        boolean ifPresent = repository.findOne(AppointmentSpecifications.appointmentAtSameTimeSpec(appointmentDate, appointmentHour)).isPresent();
        if(ifPresent){
            throw new InvalidAppointmentException("There is already an appointment with this time");
        }
    }

}
