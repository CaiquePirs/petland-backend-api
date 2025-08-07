package com.petland.modules.sale.calculator;

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

    public BigDecimal calculateProfitByItemSale(Product product, int qtdItems){
        try {
            BigDecimal profitByProduct = product.getCostSale().subtract(product.getCostPrice());
            return profitByProduct.multiply(BigDecimal.valueOf(qtdItems));

        }catch (Exception e){
            throw new RuntimeException("Error performing calculation");
        }
    }

    public BigDecimal calculateTotalItemsSale(List<ItemsSale> listItemsSale){
        if (listItemsSale == null || listItemsSale.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty.");
        }
        return listItemsSale.stream()
                .map(ItemsSale::getItemsSaleTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalListSales(List<Sale> listSales){
        if (listSales == null || listSales.isEmpty()) {
            throw new IllegalArgumentException("Sales list cannot be null or empty.");
        }
        return listSales.stream()
                .map(Sale::getTotalSales)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer sumTotalItemsSale(List<Sale> sales) {
        return sales.stream()
                .mapToInt(sale -> sale.getItemsSale().size())
                .sum();
    }

    public BigDecimal calculateProfitByItemsSale(List<ItemsSale> listItemsSale) {
        return listItemsSale.stream()
                .filter(item -> item.getProfit() != null)
                .map(ItemsSale::getProfit)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateProfitBySales(List<Sale> listSales) {
        return listSales.stream()
                .filter(sale -> sale.getProfitSale() != null)
                .map(Sale::getProfitSale)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
