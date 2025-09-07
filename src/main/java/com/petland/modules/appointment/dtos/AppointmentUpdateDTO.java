package com.petland.modules.appointment.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Schema(description = "DTO used to update an existing appointment. All fields are optional.")
public record AppointmentUpdateDTO(

        @Future(message = "Appointment date must be in the future")
        @Schema(description = "New appointment date. Must be in the future.", example = "2025-09-15")
        LocalDate appointmentDate,

        @Schema(description = "New appointment hour.", example = "16:00")
        LocalTime appointmentHour,

        @Schema(description = "Updated service type for the appointment.", implementation = ServiceType.class, example = "VET_CONSULTATION")
        ServiceType appointmentType,

        @Schema(description = "Updated status of the appointment.", implementation = AppointmentStatus.class, example = "CANCELLED")
        AppointmentStatus appointmentStatus
) {}
