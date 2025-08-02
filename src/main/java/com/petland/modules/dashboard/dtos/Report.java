package com.petland.modules.dashboard.dtos;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record Report(BigDecimal totalSales,
                     Integer itemsQuantity,
                     BigDecimal profitMargin){}

