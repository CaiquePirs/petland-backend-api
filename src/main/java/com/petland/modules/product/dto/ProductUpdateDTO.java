package com.petland.modules.product.dto;

import com.petland.modules.product.enums.ProductType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ProductUpdateDTO(String name,
                               String description,
                               String brand,
                               String supplierName,
                               ProductType productType,
                               LocalDate manufactureDate,
                               LocalDate expirationDate,
                               BigDecimal costPrice,
                               BigDecimal costSale,
                               Integer stockQuantity) {
}
