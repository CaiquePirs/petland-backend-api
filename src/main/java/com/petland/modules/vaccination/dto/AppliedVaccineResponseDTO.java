package com.petland.modules.vaccination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class AppliedVaccineResponseDTO {
   private UUID id;
   private UUID vaccinationId;
   private UUID vaccineId;
   private int quantityUsed;
}
