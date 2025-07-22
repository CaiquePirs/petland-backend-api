package com.petland.modules.vaccination.dto;

import com.petland.modules.vaccination.enums.VaccineType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record VaccineResponseDTO(UUID id,
                                 String lotNumber,
                                 String supplierName,
                                 VaccineType vaccineType,
                                 BigDecimal purchasePrice,
                                 BigDecimal priceSale,
                                 int stockQuantity,
                                 LocalDate manufactureDate,
                                 LocalDate expirationDate) {
}
