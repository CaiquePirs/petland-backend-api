package com.petland.modules.customer.dto;

import com.petland.common.Address;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponseDTO(UUID id,
                                  String name,
                                  String email,
                                  String phone,
                                  LocalDate dateBirth,
                                  Address address) {
}
