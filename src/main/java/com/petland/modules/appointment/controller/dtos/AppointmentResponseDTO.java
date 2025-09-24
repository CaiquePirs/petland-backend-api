package com.petland.modules.appointment.controller.dtos;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.model.enums.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO representing an appointment with all relevant details.")
public record AppointmentResponseDTO(

        @Schema(description = "Unique identifier of the appointment.", example = "a3b1c5e6-9f8b-4c2d-b567-123456789abc")
        UUID id,

        @Schema(description = "Unique identifier of the customer associated with the appointment.", example = "b4d2e7a8-6c9f-4f1d-b234-abcdef123456")
        UUID customerId,

        @Schema(description = "Unique identifier of the pet related to the appointment.", example = "c7e8f9d0-1b2a-4c3d-a123-654321abcdef")
        UUID petId,

        @Schema(description = "Date when the appointment is scheduled.", example = "2025-09-07")
        LocalDate appointmentDate,

        @Schema(description = "Hour when the appointment is scheduled.", example = "14:30")
        LocalTime appointmentHour,

        @Schema(description = "Type of service provided during the appointment.", implementation = ServiceType.class, example = "GENERAL_CHECK_UP")
        ServiceType appointmentType,

        @Schema(description = "Current status of the appointment.", implementation = AppointmentStatus.class, example = "COMPLETED")
        AppointmentStatus appointmentStatus
) {}

