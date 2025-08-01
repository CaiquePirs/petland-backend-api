package com.petland.modules.dashboard.dtos;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ReportsResponseDTO(
        BigDecimal totalSales,
        Integer itemsQuantity,
        BigDecimal profitMargin){}

