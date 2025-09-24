package com.petland.dashboard;

import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.calculator.BillingCalculator;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.BillingReportsService;
import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class BillingReportsServiceTest {

    @Mock private PetCareService petCareService;
    @Mock private VaccinationService vaccinationService;
    @Mock private SaleService saleService;
    @Mock private BuilderReport builder;
    @Mock private BillingCalculator billingCalculator;
    @InjectMocks BillingReportsService billingReportsService;

    private List<Vaccination> vaccinations;
    private List<Sale> sales;
    private List<PetCare> petCares;
    private LocalDate dateMin;
    private LocalDate dateMax;
    private EmployeeResponseReportDTO employeeDTO;

    @BeforeEach
    void setUp() {
        dateMin = LocalDate.now().minusMonths(5);
        dateMax = LocalDate.now().minusDays(5);

        employeeDTO = new EmployeeResponseReportDTO(UUID.randomUUID(), "Employee", Department.MANAGER);
        vaccinations = new ArrayList<>();
        sales = new ArrayList<>();
        petCares = new ArrayList<>();
        sales = new ArrayList<>();
        vaccinations.add(new Vaccination());
        sales.add(new Sale());
        petCares.add(new PetCare());
    }

    private Report report(BigDecimal totalRevenue, BigDecimal totalCostOperating, BigDecimal totalProfit, Integer totalItemsSold){
        return  Report.builder()
                .totalRevenue(totalRevenue)
                .issueDate(LocalDateTime.now())
                .operatingCost(totalCostOperating)
                .totalProfit(totalProfit)
                .itemsQuantity(totalItemsSold)
                .employee(employeeDTO)
                .build();
    }

    @Test
    @DisplayName("Should return report with correct revenue, profit, costs and items")
    void shouldReturnReportSuccessfully() {
        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(sales);
        when(vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinations);
        when(petCareService.findAllByPeriod(dateMin, dateMax)).thenReturn(petCares);

        BigDecimal totalRevenue = BigDecimal.valueOf(1800);
        BigDecimal totalProfit = BigDecimal.valueOf(1500);
        BigDecimal totalCostOperating = BigDecimal.valueOf(180);
        Integer totalItemsSold = 30;

        when(billingCalculator.calculateTotalRevenue(vaccinations, sales, petCares)).thenReturn(totalRevenue);
        when(billingCalculator.calculateTotalProfit(vaccinations, sales, petCares)).thenReturn(totalProfit);
        when(billingCalculator.totalCostOperating(vaccinations, sales, petCares)).thenReturn(totalCostOperating);
        when(billingCalculator.sumTotalItemsSold(vaccinations, sales, petCares)).thenReturn(totalItemsSold);

        Report expectedReport = report(totalRevenue, totalCostOperating, totalProfit, totalItemsSold);
        when(builder.generate(totalRevenue, totalProfit, totalItemsSold, totalCostOperating)).thenReturn(expectedReport);

        Report result = billingReportsService.generate(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(BigDecimal.valueOf(1800), result.getTotalRevenue()),
                () -> assertEquals(BigDecimal.valueOf(1500), result.getTotalProfit()),
                () -> assertEquals(BigDecimal.valueOf(180), result.getOperatingCost()),
                () -> assertEquals(30, result.getItemsQuantity()),
                () -> assertEquals(employeeDTO, result.getEmployee())
        );

        verify(saleService).findAllSalesByPeriod(dateMin, dateMax);
        verify(vaccinationService).findAllVaccinationsByPeriod(dateMin, dateMax);
        verify(petCareService).findAllByPeriod(dateMin, dateMax);
        verify(billingCalculator).calculateTotalRevenue(vaccinations, sales, petCares);
        verify(billingCalculator).calculateTotalProfit(vaccinations, sales, petCares);
        verify(billingCalculator).sumTotalItemsSold(vaccinations, sales, petCares);
        verify(billingCalculator).totalCostOperating(vaccinations, sales, petCares);
        verify(builder).generate(totalRevenue, totalProfit, totalItemsSold, totalCostOperating);
    }

    @Test
    @DisplayName("Should return report when there are sales, no vaccinations, and pet care services")
    void shouldReturnReportWithSalesNoVaccinationsPetCare() {
        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(sales);
        when(vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(List.of());
        when(petCareService.findAllByPeriod(dateMin, dateMax)).thenReturn(petCares);

        BigDecimal totalRevenue = BigDecimal.valueOf(1200);
        BigDecimal totalProfit = BigDecimal.valueOf(900);
        BigDecimal totalCostOperating = BigDecimal.valueOf(150);
        Integer totalItemsSold = 20;

        when(billingCalculator.calculateTotalRevenue(List.of(), sales, petCares)).thenReturn(totalRevenue);
        when(billingCalculator.calculateTotalProfit(List.of(), sales, petCares)).thenReturn(totalProfit);
        when(billingCalculator.totalCostOperating(List.of(), sales, petCares)).thenReturn(totalCostOperating);
        when(billingCalculator.sumTotalItemsSold(List.of(), sales, petCares)).thenReturn(totalItemsSold);

        Report expectedReport = report(totalRevenue, totalCostOperating, totalProfit, totalItemsSold);
        when(builder.generate(totalRevenue, totalProfit, totalItemsSold, totalCostOperating)).thenReturn(expectedReport);

        Report result = billingReportsService.generate(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(totalRevenue, result.getTotalRevenue()),
                () -> assertEquals(totalProfit, result.getTotalProfit()),
                () -> assertEquals(totalCostOperating, result.getOperatingCost()),
                () -> assertEquals(totalItemsSold, result.getItemsQuantity()),
                () -> assertEquals(employeeDTO, result.getEmployee())
        );
    }

    @Test
    @DisplayName("Should return report when no sales, vaccinations exist, and pet care services")
    void shouldReturnReportWithNoSalesVaccinationsAndPetCare() {
        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(List.of()); // sem vendas
        when(vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinations);
        when(petCareService.findAllByPeriod(dateMin, dateMax)).thenReturn(petCares);

        BigDecimal totalRevenue = BigDecimal.valueOf(800);
        BigDecimal totalProfit = BigDecimal.valueOf(600);
        BigDecimal totalCostOperating = BigDecimal.valueOf(100);
        Integer totalItemsSold = 10;

        when(billingCalculator.calculateTotalRevenue(vaccinations, List.of(), petCares)).thenReturn(totalRevenue);
        when(billingCalculator.calculateTotalProfit(vaccinations, List.of(), petCares)).thenReturn(totalProfit);
        when(billingCalculator.totalCostOperating(vaccinations, List.of(), petCares)).thenReturn(totalCostOperating);
        when(billingCalculator.sumTotalItemsSold(vaccinations, List.of(), petCares)).thenReturn(totalItemsSold);

        Report expectedReport = report(totalRevenue, totalCostOperating, totalProfit, totalItemsSold);
        when(builder.generate(totalRevenue, totalProfit, totalItemsSold, totalCostOperating)).thenReturn(expectedReport);

        Report result = billingReportsService.generate(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(totalRevenue, result.getTotalRevenue()),
                () -> assertEquals(totalProfit, result.getTotalProfit()),
                () -> assertEquals(totalCostOperating, result.getOperatingCost()),
                () -> assertEquals(totalItemsSold, result.getItemsQuantity()),
                () -> assertEquals(employeeDTO, result.getEmployee())
        );
    }

    @Test
    @DisplayName("Should return report when sales and vaccinations exist but no pet care services")
    void shouldReturnReportWithSalesVaccinationsNoPetCare() {
        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(sales);
        when(vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinations);
        when(petCareService.findAllByPeriod(dateMin, dateMax)).thenReturn(List.of()); // sem pet care

        BigDecimal totalRevenue = BigDecimal.valueOf(1000);
        BigDecimal totalProfit = BigDecimal.valueOf(750);
        BigDecimal totalCostOperating = BigDecimal.valueOf(120);
        Integer totalItemsSold = 15;

        when(billingCalculator.calculateTotalRevenue(vaccinations, sales, List.of())).thenReturn(totalRevenue);
        when(billingCalculator.calculateTotalProfit(vaccinations, sales, List.of())).thenReturn(totalProfit);
        when(billingCalculator.totalCostOperating(vaccinations, sales, List.of())).thenReturn(totalCostOperating);
        when(billingCalculator.sumTotalItemsSold(vaccinations, sales, List.of())).thenReturn(totalItemsSold);

        Report expectedReport = report(totalRevenue, totalCostOperating, totalProfit, totalItemsSold);
        when(builder.generate(totalRevenue, totalProfit, totalItemsSold, totalCostOperating)).thenReturn(expectedReport);

        Report result = billingReportsService.generate(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(totalRevenue, result.getTotalRevenue()),
                () -> assertEquals(totalProfit, result.getTotalProfit()),
                () -> assertEquals(totalCostOperating, result.getOperatingCost()),
                () -> assertEquals(totalItemsSold, result.getItemsQuantity()),
                () -> assertEquals(employeeDTO, result.getEmployee())
        );
    }
}
