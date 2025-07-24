package com.petland.modules.petCare.dtos;

import com.petland.common.entity.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GroomingResponseDTO(UUID id,
                                  UUID petId,
                                  UUID customerId,
                                  UUID employeeId,
                                  int groomingQuantity,
                                  BigDecimal priceCost,
                                  BigDecimal totalCost,
                                  LocalDateTime groomingMoment,
                                  Address location) {
}
