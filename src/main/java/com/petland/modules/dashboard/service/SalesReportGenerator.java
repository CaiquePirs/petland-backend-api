package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.dtos.SalesReportsDTO;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.ItemsSaleService;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.sale.util.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesReportGenerator {

    private final SaleCalculator saleCalculator;
    private final SaleService saleService;
    private final ItemsSaleService itemsSaleService;
    private final ProductService productService;

    public SalesReportsDTO totalSalesByPeriod(LocalDate dateMin, LocalDate dateMax) {
        List<Sale> salesList = saleService.findAllSalesByPeriod(dateMin, dateMax);

        if(salesList.isEmpty()){
            throw new NotFoundException("Sales reports not found");
        }

        BigDecimal totalSales = saleCalculator.calculateTotalListSales(salesList);
        BigDecimal totalProfit = saleCalculator.calculateProfitBySales(salesList);
        Integer sumItems = saleCalculator.sumTotalItemsSale(salesList);

        return SalesReportsDTO.builder()
                .itemsQuantity(sumItems)
                .totalSales(totalSales)
                .profitMargin(totalProfit)
                .build();
    }

    public SalesReportsDTO totalSalesByProductId(UUID productId){
        Product product = productService.findById(productId);
        List<ItemsSale> itemsSaleList = itemsSaleService.findAllItemsSaleByProductId(product.getId());

        if(itemsSaleList.isEmpty()){
            throw new NotFoundException("Sales reports not found");
        }

        BigDecimal totalSales = saleCalculator.calculateTotalItemsSale(itemsSaleList);
        BigDecimal totalProfit = saleCalculator.calculateProfitByItemsSale(itemsSaleList);

        return SalesReportsDTO.builder()
                .itemsQuantity(itemsSaleList.size())
                .totalSales(totalSales)
                .profitMargin(totalProfit)
                .build();
    }


}
