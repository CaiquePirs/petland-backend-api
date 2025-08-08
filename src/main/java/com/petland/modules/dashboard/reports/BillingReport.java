package com.petland.modules.dashboard.reports;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.modules.dashboard.calculator.BillingCalculator;
import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillingReport {

    private final PetCareService petCareService;
    private final VaccinationService vaccinationService;
    private final SaleService saleService;
    private final BillingCalculator billingCalculator;
    private final EmployeeService service;
    private final AccessValidator access;
    private final EmployeeMapper mapper;

    public Report generate(LocalDate dateMin, LocalDate dateMax) {
        List<Sale> sales = saleService.findAllSalesByPeriod(dateMin, dateMax);
        List<Vaccination> vaccinations = vaccinationService.findAllVaccinationsByPeriod(dateMin, dateMax);
        List<PetCare> petCareList = petCareService.findAllByPeriod(dateMin, dateMax);

        BigDecimal totalRevenue = billingCalculator.calculateTotalRevenue(vaccinations, sales, petCareList);
        BigDecimal totalProfit = billingCalculator.calculateTotalProfit(vaccinations, sales, petCareList);
        Integer totalItemsSold = billingCalculator.sumTotalItemsSold(vaccinations, sales, petCareList);
        BigDecimal totalCostOperating = billingCalculator.totalCostOperating(vaccinations, sales, petCareList);

        var employeeLogged = mapper.toReports(service.findById(access.getLoggedInUser()));

        return Report.builder()
                .totalRevenue(totalRevenue)
                .operatingCost(totalCostOperating)
                .itemsQuantity(totalItemsSold)
                .totalProfit(totalProfit)
                .issueDate(LocalDateTime.now())
                .employee(employeeLogged)
                .build();
    }
}
