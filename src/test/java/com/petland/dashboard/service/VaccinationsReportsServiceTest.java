package com.petland.dashboard.service;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.dashboard.service.VaccinationsReportsService;
import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
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
public class VaccinationsReportsServiceTest {

    @Mock private VaccinationService service;
    @Mock private VaccinationCalculator calculator;
    @Mock private BuilderReport builder;
    @InjectMocks VaccinationsReportsService reportsService;

    private BigDecimal totalBilled;
    private BigDecimal totalProfit;
    private Integer sumItemsSold;
    private BigDecimal totalCostOperating;
    private LocalDate dateMin;
    private LocalDate dateMax;
    private UUID vaccineId;

    @BeforeEach
    void setUp(){
        vaccineId = UUID.randomUUID();
        totalBilled = BigDecimal.valueOf(2000);
        totalProfit = BigDecimal.valueOf(400);
        totalCostOperating = BigDecimal.valueOf(300);
        sumItemsSold = 30;
        dateMin = LocalDate.now().minusMonths(4);
        dateMax = LocalDate.now();
    }

    private Report report(){
        return Report.builder()
                .totalRevenue(totalBilled)
                .totalProfit(totalProfit)
                .operatingCost(totalCostOperating)
                .itemsQuantity(sumItemsSold)
                .issueDate(LocalDateTime.now())
                .employee(new EmployeeResponseReportDTO(UUID.randomUUID(), "Employee", Department.MANAGER))
                .build();
    }

    @Test
    void shouldReturnReportsByPeriodSuccessfully(){
        List<Vaccination> vaccinations = new ArrayList<>();
        vaccinations.add(new Vaccination());

        when(service.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinations);
        when(calculator.calculateTotalBilledByVaccinationsList(vaccinations)).thenReturn(totalBilled);
        when(calculator.sumTotalVaccinationsApplied(vaccinations)).thenReturn(sumItemsSold);
        when(calculator.calculateTotalCostVaccine(vaccinations)).thenReturn(totalCostOperating);
        when(calculator.calculateTotalProfitByVaccinationsList(vaccinations)).thenReturn(totalProfit);
        when(builder.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating)).thenReturn(report());

        Report reportExpected = report();
        Report result = reportsService.totalByPeriod(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(vaccinations),
                () -> assertEquals(reportExpected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(reportExpected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(reportExpected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(reportExpected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(service).findAllVaccinationsByPeriod(dateMin, dateMax);
        verify(calculator).calculateTotalBilledByVaccinationsList(vaccinations);
        verify(calculator).calculateTotalProfitByVaccinationsList(vaccinations);
        verify(calculator).calculateTotalCostVaccine(vaccinations);
        verify(calculator).sumTotalVaccinationsApplied(vaccinations);
    }

    @Test
    void shouldThrowExceptionWhenVaccinationsByPeriodIsEmpty() {
        List<Vaccination> vaccinationsEmpty = new ArrayList<>();

        when(service.findAllVaccinationsByPeriod(dateMin, dateMax)).thenReturn(vaccinationsEmpty);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> reportsService.totalByPeriod(dateMin, dateMax)
        );
        assertEquals("Vaccination list reports not found", ex.getMessage());

        verify(service).findAllVaccinationsByPeriod(dateMin, dateMax);
        verify(builder, never()).generate(any(), any(), any(), any());
    }

    @Test
    void shouldReturnReportsByVaccineIdSuccessfully(){
        List<Vaccination> vaccinations = new ArrayList<>();
        vaccinations.add(new Vaccination());

        when(service.findAllVaccinationsByVaccine(vaccineId)).thenReturn(vaccinations);
        when(calculator.calculateTotalBilledByVaccinationsList(vaccinations)).thenReturn(totalBilled);
        when(calculator.sumTotalVaccinationsApplied(vaccinations)).thenReturn(sumItemsSold);
        when(calculator.calculateTotalCostVaccine(vaccinations)).thenReturn(totalCostOperating);
        when(calculator.calculateTotalProfitByVaccinationsList(vaccinations)).thenReturn(totalProfit);
        when(builder.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating)).thenReturn(report());

        Report reportExpected = report();
        Report result = reportsService.totalByVaccine(vaccineId);

        assertAll(
                () -> assertNotNull(vaccinations),
                () -> assertEquals(reportExpected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(reportExpected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(reportExpected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(reportExpected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(service).findAllVaccinationsByVaccine(vaccineId);
        verify(calculator).calculateTotalBilledByVaccinationsList(vaccinations);
        verify(calculator).calculateTotalProfitByVaccinationsList(vaccinations);
        verify(calculator).calculateTotalCostVaccine(vaccinations);
        verify(calculator).sumTotalVaccinationsApplied(vaccinations);
    }

    @Test
    void shouldThrowExceptionWhenReportByVaccineIsEmpty() {
        List<Vaccination> vaccinationsEmpty = new ArrayList<>();

        when(service.findAllVaccinationsByVaccine(vaccineId)).thenReturn(vaccinationsEmpty);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> reportsService.totalByVaccine(vaccineId)
        );
        assertEquals("Vaccination list reports not found", ex.getMessage());

        verify(service).findAllVaccinationsByVaccine(vaccineId);
        verify(builder, never()).generate(any(), any(), any(), any());
    }

}