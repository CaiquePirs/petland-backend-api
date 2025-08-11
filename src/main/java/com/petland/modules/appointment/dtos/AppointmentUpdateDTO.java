package com.petland.modules.appointment.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentUpdateDTO(@Future(message = "Appointment date must be in the future")
                                   LocalDate appointmentDate,
                                   LocalTime appointmentHour,
                                   ServiceType appointmentType,
                                   AppointmentStatus appointmentStatus) {
}
