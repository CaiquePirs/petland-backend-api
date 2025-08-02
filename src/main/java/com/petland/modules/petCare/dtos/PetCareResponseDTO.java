package com.petland.modules.petCare.dtos;

import com.petland.common.entity.Address;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record PetCareResponseDTO(UUID id,
                                 UUID petId,
                                 UUID customerId,
                                 UUID employeeId,
                                 List<PetCareDetailsResponseDTO> detailsResponseDTO,
                                 BigDecimal totalRevenue,
                                 BigDecimal totalProfit,
                                 LocalDateTime serviceDate,
                                 Address location) {
}
