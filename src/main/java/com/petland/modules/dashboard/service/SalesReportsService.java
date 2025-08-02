package com.petland.modules.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.SaleReport;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.ItemsSaleService;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesReportsService {

    private final SaleService saleService;
    private final ItemsSaleService itemsSaleService;
    private final ProductService productService;
    private final SaleReport report;

    public Report totalSalesByPeriod(LocalDate dateMin, LocalDate dateMax) {
        List<Sale> salesList = saleService.findAllSalesByPeriod(dateMin, dateMax);

        if (salesList.isEmpty()) {
            throw new NotFoundException("Sales reports not found");
        }
        return report.generateBySaleList(salesList);
    }

    public Report totalSalesByProductId(UUID productId) {
        Product product = productService.findById(productId);
        List<ItemsSale> itemsSaleList = itemsSaleService.findAllItemsSaleByProductId(product.getId());

        if (itemsSaleList.isEmpty()) {
            throw new NotFoundException("Sales reports not found");
        }
        return report.generateByItemsSaleList(itemsSaleList);
    }
}
