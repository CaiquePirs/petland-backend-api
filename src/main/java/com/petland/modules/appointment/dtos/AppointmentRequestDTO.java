package com.petland.modules.appointment.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO used to create or update an appointment.")
public record AppointmentRequestDTO(

        @NotNull(message = "Customer ID is required")
        @Schema(description = "Unique identifier of the customer associated with the appointment.", example = "b4d2e7a8-6c9f-4f1d-b234-abcdef123456")
        UUID customerId,

        @NotNull(message = "Pet ID is required")
        @Schema(description = "Unique identifier of the pet related to the appointment.",example = "c7e8f9d0-1b2a-4c3d-a123-654321abcdef")
        UUID petId,

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment date must be in the future")
        @Schema(description = "Date when the appointment is scheduled. Must be in the future.", example = "2025-09-10")
        LocalDate appointmentDate,

        @NotNull(message = "Appointment hour is required")
        @Schema(description = "Hour when the appointment is scheduled.", example = "15:45")
        LocalTime appointmentHour,

        @NotNull(message = "Appointment type is required")
        @Schema(description = "Type of service provided during the appointment.", implementation = ServiceType.class, example = "FULL_GROOMING")
        ServiceType appointmentType,

        @Schema(description = "Initial status of the appointment. If not provided, defaults may be applied.", implementation = AppointmentStatus.class, example = "PENDING")
        AppointmentStatus appointmentStatus
) {}

