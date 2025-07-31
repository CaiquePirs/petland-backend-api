package com.petland.modules.dashboard.dtos;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record SalesReportsDTO(
        BigDecimal totalSales,
        Integer itemsQuantity,
        BigDecimal profitMargin){}

