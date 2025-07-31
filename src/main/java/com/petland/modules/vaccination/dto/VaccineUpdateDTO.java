package com.petland.modules.vaccination.dto;

import com.petland.modules.vaccination.enums.VaccineType;

import java.math.BigDecimal;
import java.time.LocalDate;

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
