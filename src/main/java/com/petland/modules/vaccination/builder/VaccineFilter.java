package com.petland.modules.vaccination.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.vaccination.module.enums.VaccineType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class VaccineFilter {

    private String lotNumber;
    private String supplierName;
    private VaccineType vaccineType;
    private BigDecimal minPurchasePrice;
    private BigDecimal maxPurchasePrice;
    private BigDecimal minPriceSale;
    private BigDecimal maxPriceSale;
    private Integer minStockQuantity;
    private Integer maxStockQuantity;
    private LocalDate manufactureAfter;
    private LocalDate expirationBefore;
    private StatusEntity status;
}
