package com.petland.consultation;

import com.petland.modules.consultation.calculator.ConsultationCalculator;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.module.Vaccination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ConsultationCalculatorTest {

    private ConsultationCalculator calculator;

    static Consultation consultation() {
        Sale sale = new Sale();
        PetCare service = new PetCare();
        Vaccination vaccination = new Vaccination();

        return Consultation.builder()
                .sales(sale)
                .vaccination(vaccination)
                .service(service)
                .build();
    }

    @BeforeEach
    void setUp() {
        calculator = new ConsultationCalculator();
    }

    static Stream<Arguments> generateArguments1() {
        Consultation consultation1 = consultation();
        Consultation consultation2 = consultation();
        Consultation consultation3 = consultation();

        consultation1.getService().setTotalRevenue(BigDecimal.valueOf(345.99));
        consultation1.getSales().setTotalSales(BigDecimal.valueOf(239.76));
        consultation1.getVaccination().setTotalByVaccination(BigDecimal.valueOf(478.00));

        consultation2.getService().setTotalRevenue(BigDecimal.valueOf(0));
        consultation2.getSales().setTotalSales(BigDecimal.valueOf(0));
        consultation2.getVaccination().setTotalByVaccination(BigDecimal.valueOf(0));

        consultation3.getService().setTotalRevenue(BigDecimal.valueOf(65.97));
        consultation3.getSales().setTotalSales(BigDecimal.valueOf(0));
        consultation3.getVaccination().setTotalByVaccination(BigDecimal.valueOf(47.65));

        return Stream.of(
                Arguments.of(consultation1, BigDecimal.valueOf(1063.75)),
                Arguments.of(consultation2, BigDecimal.valueOf(0)),
                Arguments.of(consultation3, BigDecimal.valueOf(113.62))
        );
    }

    static Stream<Arguments> generateArguments2() {
        Consultation consultation1 = consultation();
        Consultation consultation2 = consultation();
        Consultation consultation3 = consultation();

        consultation1.getService().setTotalProfit(BigDecimal.valueOf(125.87));
        consultation1.getSales().setProfitSale(BigDecimal.valueOf(79.66));
        consultation1.getVaccination().setProfitByVaccination(BigDecimal.valueOf(246.00));

        consultation2.getService().setTotalProfit(BigDecimal.valueOf(0));
        consultation2.getSales().setProfitSale(BigDecimal.valueOf(0));
        consultation2.getVaccination().setProfitByVaccination(BigDecimal.valueOf(0));

        consultation3.getService().setTotalProfit(BigDecimal.valueOf(19.87));
        consultation3.getSales().setProfitSale(BigDecimal.valueOf(0));
        consultation3.getVaccination().setProfitByVaccination(BigDecimal.valueOf(25.87));

        return Stream.of(
                Arguments.of(consultation1, BigDecimal.valueOf(451.53)),
                Arguments.of(consultation2, BigDecimal.valueOf(0)),
                Arguments.of(consultation3, BigDecimal.valueOf(45.74))
        );
    }

    static Stream<Arguments> generateArguments3() {
        Consultation consultation1 = consultation();
        Consultation consultation2 = consultation();
        Consultation consultation3 = consultation();

        consultation1.getSales().setTotalSales(BigDecimal.valueOf(239.76));
        consultation1.getSales().setProfitSale(BigDecimal.valueOf(79.66));
        consultation1.getVaccination().setTotalByVaccination(BigDecimal.valueOf(478.00));
        consultation1.getVaccination().setProfitByVaccination(BigDecimal.valueOf(246.00));
        consultation1.getService().setTotalCostOperating(BigDecimal.valueOf(45.00));

        consultation2.getSales().setTotalSales(BigDecimal.valueOf(0));
        consultation2.getSales().setProfitSale(BigDecimal.valueOf(0));
        consultation2.getVaccination().setTotalByVaccination(BigDecimal.valueOf(0));
        consultation2.getVaccination().setProfitByVaccination(BigDecimal.valueOf(0));
        consultation2.getService().setTotalCostOperating(BigDecimal.valueOf(0));

        consultation3.getSales().setTotalSales(BigDecimal.valueOf(239.76));
        consultation3.getSales().setProfitSale(BigDecimal.valueOf(79.66));
        consultation3.getVaccination().setTotalByVaccination(BigDecimal.valueOf(47.65));
        consultation3.getVaccination().setProfitByVaccination(BigDecimal.valueOf(25.87));
        consultation3.getService().setTotalCostOperating(BigDecimal.valueOf(0));

        return Stream.of(
                Arguments.of(consultation1, BigDecimal.valueOf(437.10)),
                Arguments.of(consultation2, BigDecimal.valueOf(0)),
                Arguments.of(consultation3, BigDecimal.valueOf(181.88))
        );
    }

    @ParameterizedTest(name = "Billing calculation for consultation {index}: expected={1}")
    @MethodSource("generateArguments1")
    void shouldCalculateTotalBilledExact(Consultation consultation, BigDecimal valueExpected) {
        BigDecimal totalByConsultation = calculator.calculateTotalBilling(consultation);

        assertNotNull(totalByConsultation);
        assertThat(totalByConsultation).isEqualByComparingTo(valueExpected);
    }

    @ParameterizedTest(name = "Profit calculation for consultation {index}: expected={1}")
    @MethodSource("generateArguments2")
    void shouldCalculateTotalProfitExact(Consultation consultation, BigDecimal valueExpected) {
        BigDecimal profitByConsultation = calculator.calculateTotalProfit(consultation);

        assertNotNull(profitByConsultation);
        assertThat(profitByConsultation).isEqualByComparingTo(valueExpected);
    }

    @ParameterizedTest(name = "Cost Operating calculation for consultation {index}: expected={1}")
    @MethodSource("generateArguments3")
    void shouldCalculateTotalCostOperatingExact(Consultation consultation, BigDecimal valueExpected) {
        BigDecimal totalCostByConsultation = calculator.calculateTotalCostOperating(consultation);

        assertNotNull(totalCostByConsultation);
        assertThat(totalCostByConsultation).isEqualByComparingTo(valueExpected);
    }
}
