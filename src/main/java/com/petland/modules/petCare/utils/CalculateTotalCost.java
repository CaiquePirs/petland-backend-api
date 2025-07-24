package com.petland.modules.petCare.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculateTotalCost {
    public BigDecimal calculate(int quantityService, BigDecimal costService){
        BigDecimal quantity = BigDecimal.valueOf(quantityService);
        return costService.multiply(quantity);
    }
}
