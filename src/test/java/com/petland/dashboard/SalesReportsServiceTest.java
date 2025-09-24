package com.petland.dashboard;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.SalesReportsService;
import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SalesReportsServiceTest {

    @Mock private SaleService saleService;
    @Mock private SaleCalculator calculator;
    @Mock private BuilderReport builderReport;

    @InjectMocks private SalesReportsService salesReportsService;

    private BigDecimal totalBilled;
    private BigDecimal totalProfit;
    private BigDecimal totalCostByProducts;
    private Integer sumItems;
    private LocalDate dateMin;
    private LocalDate dateMax;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        totalBilled = BigDecimal.valueOf(5000);
        totalProfit = BigDecimal.valueOf(1200);
        totalCostByProducts = BigDecimal.valueOf(3800);
        sumItems = 45;
        dateMin = LocalDate.now().minusMonths(2);
        dateMax = LocalDate.now();
    }

    private Report report() {
        return Report.builder()
                .totalRevenue(totalBilled)
                .totalProfit(totalProfit)
                .operatingCost(totalCostByProducts)
                .itemsQuantity(sumItems)
                .issueDate(LocalDateTime.now())
                .employee(new EmployeeResponseReportDTO(UUID.randomUUID(), "Employee Test", Department.ATTENDANT))
                .build();
    }

    @Test
    void shouldReturnReportsByPeriodSuccessfully() {
        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale());

        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(sales);
        when(calculator.calculateTotalBilledBySaleList(sales)).thenReturn(totalBilled);
        when(calculator.sumQuantityItemsSale(sales)).thenReturn(sumItems);
        when(calculator.calculateTotalCostProducts(sales)).thenReturn(totalCostByProducts);
        when(calculator.calculateProfitBySales(sales)).thenReturn(totalProfit);
        when(builderReport.generate(totalBilled, totalProfit, sumItems, totalCostByProducts)).thenReturn(report());

        Report expected = report();
        Report result = salesReportsService.totalByPeriod(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(expected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(expected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(expected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(saleService).findAllSalesByPeriod(dateMin, dateMax);
        verify(calculator).calculateTotalBilledBySaleList(sales);
        verify(calculator).calculateProfitBySales(sales);
        verify(calculator).calculateTotalCostProducts(sales);
        verify(calculator).sumQuantityItemsSale(sales);
    }

    @Test
    void shouldThrowExceptionWhenSalesByPeriodIsEmpty() {
        List<Sale> emptySales = new ArrayList<>();

        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(emptySales);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> salesReportsService.totalByPeriod(dateMin, dateMax)
        );

        assertEquals("Sale list reports not found", ex.getMessage());

        verify(saleService).findAllSalesByPeriod(dateMin, dateMax);
        verify(builderReport, never()).generate(any(), any(), any(), any());
    }

    @Test
    void shouldReturnReportsByProductIdSuccessfully() {
        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale());

        when(saleService.findAllSalesByProductId(productId)).thenReturn(sales);
        when(calculator.calculateTotalBilledBySaleList(sales)).thenReturn(totalBilled);
        when(calculator.sumQuantityItemsSale(sales)).thenReturn(sumItems);
        when(calculator.calculateTotalCostProducts(sales)).thenReturn(totalCostByProducts);
        when(calculator.calculateProfitBySales(sales)).thenReturn(totalProfit);
        when(builderReport.generate(totalBilled, totalProfit, sumItems, totalCostByProducts)).thenReturn(report());

        Report expected = report();
        Report result = salesReportsService.totalByProductId(productId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(expected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(expected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(expected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(saleService).findAllSalesByProductId(productId);
        verify(calculator).calculateTotalBilledBySaleList(sales);
        verify(calculator).calculateProfitBySales(sales);
        verify(calculator).calculateTotalCostProducts(sales);
        verify(calculator).sumQuantityItemsSale(sales);
    }

    @Test
    void shouldThrowExceptionWhenReportByProductIdIsEmpty() {
        List<Sale> emptySales = new ArrayList<>();

        when(saleService.findAllSalesByProductId(productId)).thenReturn(emptySales);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> salesReportsService.totalByProductId(productId)
        );

        assertEquals("Sale list reports not found", ex.getMessage());

        verify(saleService).findAllSalesByProductId(productId);
        verify(builderReport, never()).generate(any(), any(), any(), any());
    }
}
