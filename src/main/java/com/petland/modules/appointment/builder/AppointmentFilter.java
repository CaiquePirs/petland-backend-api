package com.petland.modules.appointment.builder;

import com.petland.common.entity.enums.StatusEntity;
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

    private String customerId;
    private String petId;
    private AppointmentStatus appointmentStatus;
    private LocalTime appointmentHour;
    private LocalDate appointmentDate;
    private ServiceType appointmentType;
    private StatusEntity status;

    public UUID getCustomerId() {
        return customerId != null && !customerId.isBlank() ? UUID.fromString(customerId) : null;
    }

    public UUID getPetId() {
        return petId != null && !petId.isBlank() ? UUID.fromString(petId) : null;
    }
}
