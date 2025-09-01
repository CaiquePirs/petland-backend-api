package com.petland.modules.vaccination.dto;

import com.petland.modules.vaccination.enums.VaccineType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record VaccineUpdateDTO(
        String lotNumber,
        String supplierName,
        VaccineType vaccineType,
        BigDecimal purchasePrice,
        BigDecimal priceSale,
        Integer stockQuantity,
        LocalDate manufactureDate,
        LocalDate expirationDate) {
}
