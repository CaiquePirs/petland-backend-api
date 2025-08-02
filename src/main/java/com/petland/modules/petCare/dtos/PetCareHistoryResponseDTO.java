package com.petland.modules.petCare.dtos;

import com.petland.common.entity.Address;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record PetCareHistoryResponseDTO(UUID id,
                                        UUID petId,
                                        UUID customerId,
                                        UUID employeeId,
                                        List<PetCareDetailsHistoryResponseDTO> detailsResponseDTO,
                                        BigDecimal totalService,
                                        LocalDateTime serviceDate,
                                        Address location) {
}
