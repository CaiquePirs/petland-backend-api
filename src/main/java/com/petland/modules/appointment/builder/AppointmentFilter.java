package com.petland.modules.appointment.builder;

import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilter {

    private UUID customerId;
    private UUID petId;
    private AppointmentStatus status;
    private LocalTime appointmentHour;
    private LocalDate appointmentDate;
    private ServiceType appointmentType;
}
