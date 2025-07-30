package com.petland.modules.sale.util;

import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SaleCalculator {

    public BigDecimal calculateTotalSale(int quantity, BigDecimal value){
        if (quantity < 0 || value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid values for calculation.");
        }
        return BigDecimal.valueOf(quantity).multiply(value);
    }

    public BigDecimal calculateTotalItemsSale(List<ItemsSale> listItemsSale){
        if (listItemsSale == null || listItemsSale.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty.");
        }
        return listItemsSale.stream()
                .map(ItemsSale::getItemsSaleTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public BigDecimal calculateProfitByItemsSale(List<ItemsSale> listItemsSale) {
        return listItemsSale.stream()
                .map(ItemsSale::getProfit)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
