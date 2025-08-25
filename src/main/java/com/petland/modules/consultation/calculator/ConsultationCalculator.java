package com.petland.modules.consultation.calculator;

import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.module.Vaccination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ConsultationCalculator {

    public BigDecimal calculateTotalBilling(Consultation consultation) {
        return Stream.of(
                Optional.ofNullable(consultation.getSales())
                        .map(Sale::getTotalSales)
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getVaccination())
                        .map(Vaccination::getTotalByVaccination)
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getService())
                        .map(PetCare::getTotalRevenue)
                        .orElse(BigDecimal.ZERO)

        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalCostOperating(Consultation consultation) {
        return Stream.of(
                Optional.ofNullable(consultation.getSales())
                        .map(s -> s.getTotalSales().subtract(s.getProfitSale()))
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getVaccination())
                        .map(v -> v.getTotalByVaccination().subtract(v.getProfitByVaccination()))
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getService())
                        .map(PetCare::getTotalCostOperating)
                        .orElse(BigDecimal.ZERO)

        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalProfit(Consultation consultation) {
        return Stream.of(
                Optional.ofNullable(consultation.getSales())
                        .map(Sale::getProfitSale)
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getVaccination())
                        .map(Vaccination::getProfitByVaccination)
                        .orElse(BigDecimal.ZERO),

                Optional.ofNullable(consultation.getService())
                        .map(PetCare::getTotalProfit)
                        .orElse(BigDecimal.ZERO)

        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
