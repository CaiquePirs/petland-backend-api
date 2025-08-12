package com.petland.modules.vaccination.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class VaccinationFilter {

    private UUID petId;
    private UUID customerId;
    private UUID veterinarianId;
    private LocalDate vaccinationDate;
    private LocalDate nextDoseBefore;
    private StatusEntity status;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
