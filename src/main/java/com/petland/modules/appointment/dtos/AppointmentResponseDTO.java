package com.petland.modules.appointment.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(UUID id,
                                     UUID customerId,
                                     UUID petId,
                                     LocalDate appointmentDate,
                                     LocalTime appointmentHour,
                                     ServiceType appointmentType,
                                     AppointmentStatus appointmentStatus) {
}
