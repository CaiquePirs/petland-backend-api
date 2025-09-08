package com.petland.modules.vaccination.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO used for response of applied vaccines")
public class AppliedVaccineResponseDTO {

   @Schema(description = "Unique ID of the applied vaccine record", example = "123e4567-e89b-12d3-a456-426614174000")
   private UUID id;

   @Schema(description = "ID of the vaccination record", example = "123e4567-e89b-12d3-a456-426614174000")
   private UUID vaccinationId;

   @Schema(description = "ID of the vaccine used", example = "123e4567-e89b-12d3-a456-426614174000")
   private UUID vaccineId;

   @Schema(description = "Quantity of the vaccine used", example = "1")
   private int quantityUsed;
}