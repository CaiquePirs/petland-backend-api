package com.petland.modules.petCare.dtos;

import com.petland.modules.petCare.enums.PetCareType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PetCareDetailsHistoryResponseDTO(UUID id,
                                               PetCareType petCareType,
                                               BigDecimal unitPrice,
                                               int quantityService,
                                               BigDecimal totalByService,
                                               String noteService,
                                               UUID petCareId) {
}
