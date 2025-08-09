package com.petland.modules.dashboard.reports;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.calculator.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleReport {

    private final SaleCalculator saleCalculator;

    public Report generateBySales(List<Sale> salesList){
        BigDecimal totalSales = saleCalculator.calculateTotalBilledBySaleList(salesList);
        BigDecimal totalProfit = saleCalculator.calculateProfitBySales(salesList);
        Integer sumItems = saleCalculator.sumQuantityItemsSale(salesList);

        return Report.builder()
                .itemsQuantity(sumItems)
                .totalRevenue(totalSales)
                .totalProfit(totalProfit)
                .issueDate(LocalDateTime.now())
                .build();
    }

    public Report generateByItemsSale(List<ItemsSale> itemsSaleList){
        BigDecimal totalSales = saleCalculator.calculateTotalBilledByItemsSale(itemsSaleList);
        BigDecimal totalProfit = saleCalculator.calculateProfitByItemsSale(itemsSaleList);

        return Report.builder()
                .itemsQuantity(itemsSaleList.size())
                .totalRevenue(totalSales)
                .totalProfit(totalProfit)
                .issueDate(LocalDateTime.now())
                .build();
    }

}
