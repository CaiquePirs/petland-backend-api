package com.petland.dashboard;

import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.dashboard.service.PetCareReportsService;
import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
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
public class PetCareReportsServiceTest {

    @Mock private PetCareService service;
    @Mock private PetCareCalculator calculator;
    @Mock private BuilderReport builder;
    @InjectMocks PetCareReportsService petCareReportsService;

    private BigDecimal totalBilled;
    private BigDecimal totalProfit;
    private Integer sumItemsSold;
    private BigDecimal totalCostOperating;
    private LocalDate dateMin;
    private LocalDate dateMax;
    private PetCareType petCareType;

    @BeforeEach
    void setUp(){
        petCareType = PetCareType.CHECKUP;
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
        List<PetCare> petCares = new ArrayList<>();
        petCares.add(new PetCare());

        when(service.findAllByPeriod(dateMin, dateMax)).thenReturn(petCares);
        when(calculator.calculateTotalRevenueByPetCareList(petCares)).thenReturn(totalBilled);
        when(calculator.sumAllItemsSold(petCares)).thenReturn(sumItemsSold);
        when(calculator.calculateTotalCostOperatingByPetCareList(petCares)).thenReturn(totalCostOperating);
        when(calculator.calculateTotalProfitByPetCareList(petCares)).thenReturn(totalProfit);
        when(builder.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating)).thenReturn(report());

        Report reportExpected = report();
        Report result = petCareReportsService.totalByPeriod(dateMin, dateMax);

        assertAll(
                () -> assertNotNull(petCares),
                () -> assertEquals(reportExpected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(reportExpected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(reportExpected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(reportExpected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(service).findAllByPeriod(dateMin, dateMax);
        verify(calculator).calculateTotalCostOperatingByPetCareList(petCares);
        verify(calculator).calculateTotalProfitByPetCareList(petCares);
        verify(calculator).calculateTotalRevenueByPetCareList(petCares);
        verify(calculator).sumAllItemsSold(petCares);
    }

    @Test
    void shouldThrowExceptionWhenPetCareByPeriodIsEmpty() {
        List<PetCare> petCaresEmpty = new ArrayList<>();

        when(service.findAllByPeriod(dateMin, dateMax)).thenReturn(petCaresEmpty);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> petCareReportsService.totalByPeriod(dateMin, dateMax)
        );
        assertEquals("PetCare list reports not found", ex.getMessage());

        verify(service).findAllByPeriod(dateMin, dateMax);
        verify(builder, never()).generate(any(), any(), any(), any());
    }

    @Test
    void shouldReturnReportsByPetCareTypeSuccessfully(){
        List<PetCare> petCares = new ArrayList<>();
        petCares.add(new PetCare());

        when(service.findAllByPetCareType(petCareType)).thenReturn(petCares);
        when(calculator.calculateTotalRevenueByPetCareList(petCares)).thenReturn(totalBilled);
        when(calculator.sumAllItemsSold(petCares)).thenReturn(sumItemsSold);
        when(calculator.calculateTotalCostOperatingByPetCareList(petCares)).thenReturn(totalCostOperating);
        when(calculator.calculateTotalProfitByPetCareList(petCares)).thenReturn(totalProfit);
        when(builder.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating)).thenReturn(report());

        Report reportExpected = report();
        Report result = petCareReportsService.totalByServiceType(String.valueOf(petCareType));

        assertAll(
                () -> assertNotNull(petCares),
                () -> assertEquals(reportExpected.getOperatingCost(), result.getOperatingCost()),
                () -> assertEquals(reportExpected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(reportExpected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(reportExpected.getItemsQuantity(), result.getItemsQuantity())
        );

        verify(service).findAllByPetCareType(petCareType);
        verify(calculator).calculateTotalCostOperatingByPetCareList(petCares);
        verify(calculator).calculateTotalProfitByPetCareList(petCares);
        verify(calculator).calculateTotalRevenueByPetCareList(petCares);
        verify(calculator).sumAllItemsSold(petCares);
    }

    @Test
    void shouldThrowExceptionWhenReportByTypeIsEmpty() {
        List<PetCare> petCaresEmpty = new ArrayList<>();

        when(service.findAllByPetCareType(petCareType)).thenReturn(petCaresEmpty);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> petCareReportsService.totalByServiceType(String.valueOf(petCareType))
        );
        assertEquals("PetCare list reports not found", ex.getMessage());

        verify(service).findAllByPetCareType(petCareType);
        verify(builder, never()).generate(any(), any(), any(), any());
    }
}
