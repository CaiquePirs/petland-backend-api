package com.petland.modules.vaccination.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationFilter {

    private String petId;
    private String customerId;
    private String veterinarianId;
    private LocalDate vaccinationDate;
    private LocalDate nextDoseBefore;
    private StatusEntity status;

    public UUID getPetId() {
        return petId != null && !petId.isBlank() ? UUID.fromString(petId) : null;
    }

    public UUID getCustomerId() {
        return customerId != null && !customerId.isBlank() ? UUID.fromString(customerId) : null;
    }

    public UUID getVeterinarianId() {
        return veterinarianId != null && !veterinarianId.isBlank() ? UUID.fromString(veterinarianId) : null;
    }
}
