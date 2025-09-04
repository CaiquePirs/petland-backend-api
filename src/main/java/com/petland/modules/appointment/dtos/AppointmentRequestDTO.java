package com.petland.modules.appointment.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder
public record AppointmentRequestDTO(@NotNull(message = "Customer ID is required")
                                    UUID customerId,

                                    @NotNull(message = "Pet ID is required")
                                    UUID petId,

                                    @NotNull(message = "Appointment date is required")
                                    @Future(message = "Appointment date must be in the future")
                                    LocalDate appointmentDate,

                                    @NotNull(message = "Appointment hour is required")
                                    LocalTime appointmentHour,

                                    @NotNull(message = "Appointment type is required")
                                    ServiceType appointmentType,

                                    AppointmentStatus appointmentStatus) {
}
