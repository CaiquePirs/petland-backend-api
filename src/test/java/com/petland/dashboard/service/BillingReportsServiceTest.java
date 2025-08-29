package com.petland.dashboard.service;

import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.calculator.BillingCalculator;
import com.petland.modules.dashboard.report.Report;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingReportsServiceTest {

    @Mock private PetCareService petCareService;
    @Mock private VaccinationService vaccinationService;
    @Mock private SaleService saleService;
    @Mock private BillingCalculator billingCalculator;
    @Mock private BuilderReport builder;
    @InjectMocks private BillingReportsService billingReportsService;

    private LocalDate dateMin;
    private LocalDate dateMax;

    private Report report;

    @BeforeEach
    void setUp() {
        dateMin = LocalDate.now().minusMonths(1);
        dateMax = LocalDate.now();

        report = Report.builder()
                .totalRevenue(BigDecimal.valueOf(1000))
                .totalProfit(BigDecimal.valueOf(400))
                .operatingCost(BigDecimal.valueOf(200))
                .itemsQuantity(10)
                .issueDate(LocalDateTime.now())
                .employee(new EmployeeResponseReportDTO(UUID.randomUUID(), "Tester", Department.FINANCIAL))
                .build();
    }

    static Stream<Arguments> provideScenarios() {
        return Stream.of(
                Arguments.of("when has only sales", List.of(new Sale()), List.of(), List.of()),
                Arguments.of("when has only vaccinations", List.of(), List.of(new Vaccination()), List.of()),
                Arguments.of("when has only petcare", List.of(), List.of(), List.of(new PetCare())),
                Arguments.of("when has all", List.of(new Sale()), List.of(new Vaccination()), List.of(new PetCare())),
                Arguments.of("when vaccinations empty", List.of(new Sale()), List.of(), List.of(new PetCare())),
                Arguments.of("when sales empty", List.of(), List.of(new Vaccination()), List.of(new PetCare())),
                Arguments.of("when petcare empty", List.of(new Sale()), List.of(new Vaccination()), List.of())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideScenarios")
    void shouldGenerateBillingReport(
            String scenario,
            List<Sale> sales,
            List<Vaccination> vaccinations,
            List<PetCare> petCareList
    ) {
        when(saleService.findAllSalesByPeriod(dateMin, dateMax)).thenReturn(sales);
        when(vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinations);
        when(petCareService.findAllByPeriod(dateMin, dateMax)).thenReturn(petCareList);

        when(billingCalculator.calculateTotalRevenue(vaccinations, sales, petCareList)).thenReturn(report.getTotalRevenue());
        when(billingCalculator.calculateTotalProfit(vaccinations, sales, petCareList)).thenReturn(report.getTotalProfit());
        when(billingCalculator.totalCostOperating(vaccinations, sales, petCareList)).thenReturn(report.getOperatingCost());
        when(billingCalculator.sumTotalItemsSold(vaccinations, sales, petCareList)).thenReturn(report.getItemsQuantity());

        when(builder.generate(
                report.getTotalRevenue(),
                report.getTotalProfit(),
                report.getItemsQuantity(),
                report.getOperatingCost()
        )).thenReturn(report);

        Report result = billingReportsService.generate(dateMin, dateMax);

        assertNotNull(result);
        assertEquals(report.getTotalRevenue(), result.getTotalRevenue());
        assertEquals(report.getTotalProfit(), result.getTotalProfit());
        assertEquals(report.getOperatingCost(), result.getOperatingCost());
        assertEquals(report.getItemsQuantity(), result.getItemsQuantity());

        verify(saleService).findAllSalesByPeriod(dateMin, dateMax);
        verify(vaccinationService).findAllVaccinationsByPeriod(dateMin, dateMax);
        verify(petCareService).findAllByPeriod(dateMin, dateMax);

        verify(billingCalculator).calculateTotalRevenue(vaccinations, sales, petCareList);
        verify(billingCalculator).calculateTotalProfit(vaccinations, sales, petCareList);
        verify(billingCalculator).totalCostOperating(vaccinations, sales, petCareList);
        verify(billingCalculator).sumTotalItemsSold(vaccinations, sales, petCareList);
        verify(builder).generate(report.getTotalRevenue(), report.getTotalProfit(), report.getItemsQuantity(), report.getOperatingCost());
    }
}
