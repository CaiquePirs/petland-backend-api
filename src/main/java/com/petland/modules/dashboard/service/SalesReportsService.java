package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesReportsService {

    private final SaleService saleService;
    private final SaleCalculator calculator;
    private final BuilderReport builderReport;

    public Report totalByPeriod(LocalDate dateMin, LocalDate dateMax) {
        List<Sale> sales = saleService.findAllSalesByPeriod(dateMin, dateMax);
        return generate(sales);
    }

    public Report totalByProductId(UUID productId) {
        List<Sale> sales = saleService.findAllSalesByProductId(productId);
        return generate(sales);
    }

    private Report generate(List<Sale> sales){
        validateIfListSalesIsEmpty(sales);
        BigDecimal totalBilled = calculator.calculateTotalBilledBySaleList(sales);
        BigDecimal totalProfit = calculator.calculateProfitBySales(sales);
        BigDecimal totalCostByProducts = calculator.calculateTotalCostProducts(sales);
        Integer sumItems = calculator.sumQuantityItemsSale(sales);
        return builderReport.generate(totalBilled, totalProfit, sumItems, totalCostByProducts);
    }

    private void validateIfListSalesIsEmpty(List<Sale> sales){
        if(sales.isEmpty()){
            throw new NotFoundException("Sale list reports not found");
        }
    }

}
