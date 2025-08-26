package com.petland.modules.customer.dto;

import com.petland.common.entity.Address;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CustomerResponseDTO(UUID id,
                                  String name,
                                  String email,
                                  String phone,
                                  LocalDate dateBirth,
                                  Address address) {
}
