package com.petland.modules.dashboard.util;

import com.petland.modules.dashboard.dtos.ReportsResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.util.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateSaleReport {

    private final SaleCalculator saleCalculator;

    public ReportsResponseDTO generateBySaleList(List<Sale> salesList){
        BigDecimal totalSales = saleCalculator.calculateTotalListSales(salesList);
        BigDecimal totalProfit = saleCalculator.calculateProfitBySales(salesList);
        Integer sumItems = saleCalculator.sumTotalItemsSale(salesList);

        return ReportsResponseDTO.builder()
                .itemsQuantity(sumItems)
                .totalSales(totalSales)
                .profitMargin(totalProfit)
                .build();
    }

    public ReportsResponseDTO generateByItemsSaleList(List<ItemsSale> itemsSaleList){
        BigDecimal totalSales = saleCalculator.calculateTotalItemsSale(itemsSaleList);
        BigDecimal totalProfit = saleCalculator.calculateProfitByItemsSale(itemsSaleList);

        return ReportsResponseDTO.builder()
                .itemsQuantity(itemsSaleList.size())
                .totalSales(totalSales)
                .profitMargin(totalProfit)
                .build();
    }

}
