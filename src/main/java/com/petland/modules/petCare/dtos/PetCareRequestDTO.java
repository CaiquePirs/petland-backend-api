package com.petland.modules.petCare.dtos;

import com.petland.common.entity.Address;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PetCareRequestDTO(@NotNull(message = "Pet ID is required")
                                UUID petId,

                                @NotNull(message = "Customer ID is required")
                                UUID customerId,

                                @NotNull(message = "Employee ID is required")
                                UUID employeeId,

                                @NotNull(message = "Services details is required")
                                List<PetCareDetailsRequestDTO> serviceDetailsList,

                                @NotNull(message = "Location is required")
                                Address location) {
}
