package com.petland.petCare;

import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.Collections.emptyList;

class PetCareCalculatorTest {

    private PetCareCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PetCareCalculator();
    }

    @Test
    void shouldCalculateTotalRevenueByQuantityAndCost() {
        BigDecimal result = calculator.calculateTotalRevenue(5, BigDecimal.valueOf(20));
        assertEquals(BigDecimal.valueOf(100), result);
    }

    @Test
    void shouldCalculateTotalRevenueByPetCareList() {
        PetCare pc1 = PetCare.builder().totalRevenue(BigDecimal.valueOf(200)).build();
        PetCare pc2 = PetCare.builder().totalRevenue(BigDecimal.valueOf(100)).build();

        BigDecimal result = calculator.calculateTotalRevenueByPetCareList(List.of(pc1, pc2));

        assertEquals(BigDecimal.valueOf(300), result);
    }

    @Test
    void shouldReturnZeroWhenPetCareListIsEmptyForRevenue() {
        BigDecimal result = calculator.calculateTotalRevenueByPetCareList(emptyList());
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldCalculateTotalCostOperatingByPetCareList() {
        PetCare pc1 = PetCare.builder().totalCostOperating(BigDecimal.valueOf(75.98)).build();
        PetCare pc2 = PetCare.builder().totalCostOperating(BigDecimal.valueOf(50.87)).build();

        BigDecimal result = calculator.calculateTotalCostOperatingByPetCareList(List.of(pc1, pc2));

        assertEquals(BigDecimal.valueOf(126.85), result); // 75.98 + 50.87 = 126.85
    }

    @Test
    void shouldCalculateTotalProfitByPetCareList() {
        PetCare pc1 = PetCare.builder().totalProfit(BigDecimal.valueOf(170)).build();
        PetCare pc2 = PetCare.builder().totalProfit(BigDecimal.valueOf(235)).build();

        BigDecimal result = calculator.calculateTotalProfitByPetCareList(List.of(pc1, pc2));

        assertEquals(BigDecimal.valueOf(405), result); // 170 + 235 = 405
    }

    @Test
    void shouldCalculateTotalProfitSubtractingCosts() {
        BigDecimal result = calculator.calculateTotalProfit(BigDecimal.valueOf(200), BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(150), result);
    }

    @Test
    void shouldReturnZeroWhenTotalServiceOrCostIsNull() {
        assertEquals(BigDecimal.ZERO, calculator.calculateTotalProfit(null, BigDecimal.TEN));
        assertEquals(BigDecimal.ZERO, calculator.calculateTotalProfit(BigDecimal.TEN, null));
    }

    @Test
    void shouldCalculateTotalRevenueByServiceList() {
        PetCareDetails d1 = PetCareDetails.builder().totalByService(BigDecimal.valueOf(200)).build();
        PetCareDetails d2 = PetCareDetails.builder().totalByService(BigDecimal.valueOf(345.87)).build();

        BigDecimal result = calculator.calculateTotalRevenueByServiceList(List.of(d1, d2));

        assertEquals(BigDecimal.valueOf(545.87), result); // 200 + 345.87
    }

    @Test
    void shouldCalculateTotalProfitByServiceList() {
        PetCareDetails d1 = PetCareDetails.builder().profitByService(BigDecimal.valueOf(200)).build();
        PetCareDetails d2 = PetCareDetails.builder().profitByService(BigDecimal.valueOf(345.87)).build();

        BigDecimal result = calculator.calculateTotalProfitByServiceList(List.of(d1, d2));

        assertEquals(BigDecimal.valueOf(545.87), result); // 200 + 345.87
    }

    @Test
    void shouldCalculateTotalCostOperatingByServiceList() {
        PetCareDetails d1 = PetCareDetails.builder().operatingCost(BigDecimal.valueOf(200)).build();
        PetCareDetails d2 = PetCareDetails.builder().operatingCost(BigDecimal.valueOf(345.87)).build();

        BigDecimal result = calculator.calculateTotalCostOperatingByServiceList(List.of(d1, d2));

        assertEquals(BigDecimal.valueOf(545.87), result); // 200 + 345.87
    }

    @Test
    void shouldSumAllItemsSoldFromPetCareList() {
        PetCareDetails d1 = PetCareDetails.builder().totalByService(BigDecimal.valueOf(200)).build();
        PetCareDetails d2 = PetCareDetails.builder().totalByService(BigDecimal.valueOf(345.87)).build();

        PetCare pc1 = PetCare.builder().petCareDetails(List.of(d1)).build();
        PetCare pc2 = PetCare.builder().petCareDetails(List.of(d2)).build();

        Integer result = calculator.sumAllItemsSold(List.of(pc1, pc2));

        assertEquals(2, result); // 1 detalhe em cada PetCare
    }

    @Test
    void shouldReturnZeroWhenPetCareListIsEmptyForItems() {
        Integer result = calculator.sumAllItemsSold(emptyList());
        assertEquals(0, result);
    }
}
