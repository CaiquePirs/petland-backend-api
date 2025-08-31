package com.petland.modules.sale.calculator;

import com.petland.modules.product.model.Product;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SaleCalculator {

    public BigDecimal calculateTotalSale(int quantity, BigDecimal value){
        if (quantity < 0 || value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Invalid values for calculation.");
        }
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateProfitByItemSale(Product product, int qtdItems){
        try {
            BigDecimal profitByProduct = product.getCostSale().subtract(product.getCostPrice());
            return profitByProduct.multiply(BigDecimal.valueOf(qtdItems));

        }catch (Exception e){
            throw new RuntimeException("Error performing calculation");
        }
    }

    public BigDecimal calculateTotalBilledByItemsSale(List<ItemsSale> listItemsSale){
        return listItemsSale
                .stream()
                .map(ItemsSale::getItemsSaleTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalBilledBySaleList(List<Sale> listSales){
        return listSales.stream()
                .map(Sale::getTotalSales)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer sumQuantityItemsSale(List<Sale> sales) {
        return sales.stream().mapToInt(sale -> sale.getItemsSale().size()).sum();
    }

    public BigDecimal calculateProfitByItemsSale(List<ItemsSale> listItemsSale) {
        return listItemsSale
                .stream()
                .map(ItemsSale::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateProfitBySales(List<Sale> listSales) {
        return listSales.stream()
                .map(Sale::getProfitSale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalCostProducts(List<Sale> sales){
        return sales.stream()
                .flatMap(s -> s.getItemsSale().stream())
                .map(item -> item.getProduct().getCostPrice().multiply(BigDecimal.valueOf(item.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
