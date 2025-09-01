package com.petland.vaccination;

import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VaccinationCalculatorTest {

    private VaccinationCalculator calculator;

    @BeforeEach
    void setUp(){
        calculator = new VaccinationCalculator();
    }

    @Test
    void shouldCalculateTotalVaccination() {
        AppliedVaccine a1 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().priceSale(BigDecimal.valueOf(20)).build())
                .quantityUsed(2)
                .build();

        AppliedVaccine a2 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().priceSale(BigDecimal.valueOf(50)).build())
                .quantityUsed(1)
                .build();

        BigDecimal total = calculator.calculateTotalVaccination(List.of(a1, a2));

        assertThat(total).isEqualByComparingTo("90");
    }

    @Test
    void shouldCalculateProfitByVaccineApplied() {
        AppliedVaccine a1 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().purchasePrice(BigDecimal.TEN).priceSale(BigDecimal.valueOf(20)).build())
                .quantityUsed(3)
                .build();

        AppliedVaccine a2 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().purchasePrice(BigDecimal.valueOf(5)).priceSale(BigDecimal.valueOf(8)).build())
                .quantityUsed(2)
                .build();

        BigDecimal profit = calculator.calculateProfitByVaccineApplied(List.of(a1, a2));

        assertThat(profit).isEqualByComparingTo("36");
    }

    @Test
    void shouldCalculateTotalBilledByVaccinationsList() {
        Vaccination v1 = Vaccination.builder().totalByVaccination(BigDecimal.valueOf(100)).build();
        Vaccination v2 = Vaccination.builder().totalByVaccination(BigDecimal.valueOf(50)).build();

        BigDecimal total = calculator.calculateTotalBilledByVaccinationsList(List.of(v1, v2));

        assertThat(total).isEqualByComparingTo("150");
    }

    @Test
    void shouldCalculateTotalProfitByVaccinationsList() {
        Vaccination v1 = Vaccination.builder().profitByVaccination(BigDecimal.valueOf(30)).build();
        Vaccination v2 = Vaccination.builder().profitByVaccination(BigDecimal.valueOf(20)).build();

        BigDecimal total = calculator.calculateTotalProfitByVaccinationsList(List.of(v1, v2));

        assertThat(total).isEqualByComparingTo("50");
    }

    @Test
    void shouldSumTotalVaccinationsApplied() {
        AppliedVaccine a1 = AppliedVaccine.builder().build();
        AppliedVaccine a2 = AppliedVaccine.builder().build();
        AppliedVaccine a3 = AppliedVaccine.builder().build();

        Vaccination v1 = Vaccination.builder().appliedVaccines(List.of(a1, a2)).build();
        Vaccination v2 = Vaccination.builder().appliedVaccines(List.of(a3)).build();

        int total = calculator.sumTotalVaccinationsApplied(List.of(v1, v2));

        assertThat(total).isEqualTo(3);
    }

    @Test
    void shouldCalculateTotalCostVaccine() {
        AppliedVaccine a1 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().purchasePrice(BigDecimal.valueOf(5)).build())
                .quantityUsed(2)
                .build();

        AppliedVaccine a2 = AppliedVaccine.builder()
                .vaccine(Vaccine.builder().purchasePrice(BigDecimal.valueOf(2)).build())
                .quantityUsed(3)
                .build();

        Vaccination vaccination = Vaccination.builder().appliedVaccines(List.of(a1, a2)).build();

        BigDecimal total = calculator.calculateTotalCostVaccine(List.of(vaccination));

        assertThat(total).isEqualByComparingTo("16");
    }
}
