package com.petland.modules.dashboard.reports;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.calculator.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleReport {

    private final SaleCalculator saleCalculator;

    public Report generateBySaleList(List<Sale> salesList){
        BigDecimal totalSales = saleCalculator.calculateTotalListSales(salesList);
        BigDecimal totalProfit = saleCalculator.calculateProfitBySales(salesList);
        Integer sumItems = saleCalculator.sumTotalItemsSale(salesList);

        return Report.builder()
                .itemsQuantity(sumItems)
                .totalRevenue(totalSales)
                .totalProfit(totalProfit)
                .build();
    }

    public Report generateByItemsSaleList(List<ItemsSale> itemsSaleList){
        BigDecimal totalSales = saleCalculator.calculateTotalItemsSale(itemsSaleList);
        BigDecimal totalProfit = saleCalculator.calculateProfitByItemsSale(itemsSaleList);

        return Report.builder()
                .itemsQuantity(itemsSaleList.size())
                .totalRevenue(totalSales)
                .totalProfit(totalProfit)
                .build();
    }

}
