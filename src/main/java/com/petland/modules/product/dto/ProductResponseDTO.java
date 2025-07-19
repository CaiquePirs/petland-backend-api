package com.petland.modules.product.dto;

import com.petland.modules.product.enums.ProductType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductResponseDTO(UUID id,
                                 String name,
                                 String description,
                                 String brand,
                                 String supplierName,
                                 ProductType productType,
                                 LocalDate manufactureDate,
                                 LocalDate expirationDate,
                                 UUID barCode,
                                 BigDecimal costPrice,
                                 BigDecimal costSale,
                                 int stockQuantity,
                                 UUID employerId) {
}
