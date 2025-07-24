package com.petland.modules.petCare.dtos;

import com.petland.common.entity.Address;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.util.UUID;

public record BathRequestDTO(@NotNull(message = "Pet ID is required")
                             UUID petId,

                             @NotNull(message = "Customer ID is required")
                             UUID customerId,

                             @NotNull(message = "Employee ID is required")
                             UUID employeeId,

                             @NotNull(message = "Bath quantity is required")
                             int bathQuantity,

                             @NotNull(message = "Price cost is required")
                             BigDecimal priceCost,

                             @DefaultValue(value = "")
                             String bathNotes,

                             @NotNull(message = "Location is required")
                             Address location) {
}
