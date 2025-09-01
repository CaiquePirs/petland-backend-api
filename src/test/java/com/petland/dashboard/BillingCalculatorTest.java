package com.petland.dashboard;

import com.petland.modules.dashboard.calculator.BillingCalculator;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.product.model.Product;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.VaccineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BillingCalculatorTest {

     private BillingCalculator billingCalculator;
     @InjectMocks private VaccinationCalculator vaccinationCalculator;

     private List<Vaccination> vaccinations;
     private List<Sale> sales;
     private List<PetCare> petCares;

    @BeforeEach
    void setUp() {
        SaleCalculator saleCalculator = new SaleCalculator();
        PetCareCalculator petCareCalculator = new PetCareCalculator();
        vaccinationCalculator = new VaccinationCalculator();
        billingCalculator = new BillingCalculator(vaccinationCalculator, saleCalculator, petCareCalculator);

        vaccinations = new ArrayList<>();
        sales = new ArrayList<>();
        petCares = new ArrayList<>();

        vaccinations.add(vaccination());
        vaccinations.add(vaccination());

        sales.add(sale());
        sales.add(sale());

        petCares.add(petCare());
        petCares.add(petCare());
    }

    private Vaccination vaccination(){
        Vaccine vaccine = Vaccine.builder().purchasePrice(BigDecimal.valueOf(10)).priceSale(BigDecimal.valueOf(20)).build();
        AppliedVaccine appliedVaccine = AppliedVaccine.builder().quantityUsed(2).vaccine(vaccine).build();
        List<AppliedVaccine> appliedVaccines = new ArrayList<>(); appliedVaccines.add(appliedVaccine);

        return Vaccination.builder()
                .totalByVaccination(BigDecimal.valueOf(300))
                .profitByVaccination(BigDecimal.valueOf(250))
                .appliedVaccines(appliedVaccines)
                .build();
    }

    private PetCare petCare(){
        PetCareDetails details = PetCareDetails.builder().build();
        List<PetCareDetails> petCareDetails = new ArrayList<>(); petCareDetails.add(details);

        return PetCare.builder()
                .totalRevenue(BigDecimal.valueOf(300))
                .totalProfit(BigDecimal.valueOf(250))
                .totalCostOperating(BigDecimal.valueOf(50))
                .petCareDetails(petCareDetails)
                .build();
    }

    private Sale sale(){
        Product product = Product.builder().costSale(BigDecimal.valueOf(20)).costPrice(BigDecimal.valueOf(10)).build();
        ItemsSale itemsSale = ItemsSale.builder().productQuantity(2).product(product).build();
        List<ItemsSale> itemsSales = new ArrayList<>(); itemsSales.add(itemsSale);

        return Sale.builder()
                .totalSales(BigDecimal.valueOf(300))
                .profitSale(BigDecimal.valueOf(250))
                .itemsSale(itemsSales)
                .build();
    }

    @Test
    void shouldCalculateBillingSuccessfully(){
        BigDecimal totalByVaccinations = vaccinations.stream()
                .map(Vaccination::getTotalByVaccination).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBySales = sales.stream().map(Sale::getTotalSales)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalByServices = petCares.stream().map(PetCare::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBilling = billingCalculator.calculateTotalRevenue(vaccinations, sales, petCares);
        assertEquals(BigDecimal.valueOf(1800), totalBilling);

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(600), totalByVaccinations),
                () -> assertEquals(BigDecimal.valueOf(600), totalBySales),
                () -> assertEquals(BigDecimal.valueOf(600), totalByServices)
        );
    }

    @Test
    void shouldCalculateTotalProfitSuccessfully(){
        sales.forEach(s -> s.setProfitSale(BigDecimal.ZERO));

        BigDecimal totalByVaccinations = vaccinations.stream()
                .map(Vaccination::getProfitByVaccination).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBySales = sales.stream().map(Sale::getProfitSale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalByServices = petCares.stream().map(PetCare::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBilling = billingCalculator.calculateTotalProfit(vaccinations, sales, petCares);
        assertEquals(BigDecimal.valueOf(1000), totalBilling);

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(500), totalByVaccinations),
                () -> assertEquals(BigDecimal.valueOf(0), totalBySales),
                () -> assertEquals(BigDecimal.valueOf(500), totalByServices)
        );
    }

    @Test
    void shouldSumAllItemsSoldSuccessfully(){
        Integer itemsSold = sales.stream().mapToInt(s -> s.getItemsSale().size()).sum();
        Integer vaccinationsSold = vaccinations.stream().mapToInt(v -> v.getAppliedVaccines().size()).sum();
        Integer servicesSold = petCares.stream().mapToInt(p -> p.getPetCareDetails().size()).sum();

        Integer totalServicesSold = billingCalculator.sumTotalItemsSold(vaccinations, sales, petCares);
        assertEquals(6, totalServicesSold);

        assertAll(
                () -> assertEquals(2, itemsSold),
                () -> assertEquals(2, vaccinationsSold),
                () -> assertEquals(2, servicesSold)
        );
    }

    @Test
    void shouldCalculateTotalCostOperatingSuccessfully(){
        BigDecimal totalCostOperating = billingCalculator.totalCostOperating(vaccinations, sales, petCares);
        assertEquals(BigDecimal.valueOf(180), totalCostOperating);
    }
}
