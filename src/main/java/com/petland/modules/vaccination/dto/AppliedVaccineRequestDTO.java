package com.petland.modules.vaccination.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AppliedVaccineRequestDTO(@NotNull(message = "Vaccine ID is required")
                                       UUID vaccineId,

                                       @NotNull(message = "Quantity of vaccine used is required")
                                       Integer quantityUsed) {
}
