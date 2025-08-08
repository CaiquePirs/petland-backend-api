package com.petland.modules.dashboard.calculator;

import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.module.Vaccination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BillingCalculator {

    private final VaccinationCalculator vaccinationCalculator;
    private final SaleCalculator saleCalculator;
    private final PetCareCalculator petCareCalculator;

    public BigDecimal calculateTotalRevenue(List<Vaccination> vaccinations, List<Sale> sales, List<PetCare> petCareList){
        BigDecimal totalRevenueByVaccinations = vaccinationCalculator.calculateTotalBilledByVaccinationsList(vaccinations);
        BigDecimal totalRevenueBySales = saleCalculator.calculateTotalBilledBySaleList(sales);
        BigDecimal totalRevenueByServicesPetCare = petCareCalculator.calculateTotalRevenueByPetCareList(petCareList);

        return Stream.of(totalRevenueByVaccinations, totalRevenueBySales, totalRevenueByServicesPetCare)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalProfit(List<Vaccination> vaccinations, List<Sale> sales, List<PetCare> petCareList) {
        BigDecimal totalProfitByVaccinations = vaccinationCalculator.calculateTotalProfitByVaccinationsList(vaccinations);
        BigDecimal totalProfitBySales = saleCalculator.calculateProfitBySales(sales);
        BigDecimal totalProfitByServicesPetCare = petCareCalculator.calculateTotalProfitByPetCareList(petCareList);

        return Stream.of(totalProfitByVaccinations, totalProfitBySales, totalProfitByServicesPetCare)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public int sumTotalItemsSold(List<Vaccination> vaccinations, List<Sale> sales, List<PetCare> petCareList){
        Integer totalVaccinationsSold = Optional.ofNullable(vaccinationCalculator.sumTotalVaccinationsApplied(vaccinations)).orElse(0);
        Integer totalItemsSaleSold = Optional.ofNullable(saleCalculator.sumQuantityItemsSale(sales)).orElse(0);
        Integer totalServicesProvided = Optional.ofNullable(petCareCalculator.sumAllItemsSold(petCareList)).orElse(0);

        return Stream.of(totalVaccinationsSold, totalItemsSaleSold, totalServicesProvided)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public BigDecimal totalCostOperating(List<Vaccination> vaccinations, List<Sale> sales, List<PetCare> petCareList){
        BigDecimal totalCostOperatingByServices = petCareCalculator.calculateTotalCostOperatingByPetCareList(petCareList);
        BigDecimal totalCostOperatingByVaccinations = vaccinationCalculator.calculateTotalCostVaccine(vaccinations);
        BigDecimal totalCostOperatingBySales = saleCalculator.calculateTotalCostProducts(sales);

        return Stream.of(totalCostOperatingByServices, totalCostOperatingByVaccinations, totalCostOperatingBySales)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}
