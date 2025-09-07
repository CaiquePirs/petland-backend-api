package com.petland.dashboard.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccinationRepository;
import com.petland.modules.vaccination.repository.VaccineRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DashboardBillingIntegrationTest {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private SaleRepository saleRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private VaccinationRepository vaccinationRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private PetCareRepository petCareRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private Employee employee;
    private Customer customer;
    private Pet pet;
    private Vaccine vaccine;
    private Product product;

    @BeforeEach
    void cleanBeforeTest(){
        petCareRepository.deleteAll();
        vaccinationRepository.deleteAll();
        saleRepository.deleteAll();
        vaccineRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();

        employee = TestUtils.employee();
        employeeRepository.save(employee);

        customer = TestUtils.customer();
        pet = TestUtils.pet(); pet.setOwner(customer); customer.getMyPets().add(pet);
        customerRepository.save(customer);

        product = TestUtils.product();
        product.setCostSale(BigDecimal.valueOf(20.00));
        product.setCostPrice(BigDecimal.valueOf(10.00));
        productRepository.save(product);

        vaccine = TestUtils.vaccine();
        vaccine.setPriceSale(BigDecimal.valueOf(20.00));
        vaccine.setPurchasePrice(BigDecimal.valueOf(10.00));
        vaccineRepository.save(vaccine);
    }

    @AfterEach
    void cleanAfterTest(){
        petCareRepository.deleteAll();
        vaccinationRepository.deleteAll();
        saleRepository.deleteAll();
        vaccineRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private Vaccination vaccination(){
        Vaccination vaccination = TestUtils.vaccination();
        vaccination.getAppliedVaccines().forEach(a -> a.setVaccine(vaccine));
        vaccination.setTotalByVaccination(BigDecimal.valueOf(200.00));
        vaccination.setProfitByVaccination(BigDecimal.valueOf(100.00));
        vaccination.setVeterinarian(employee);
        vaccination.setCustomer(customer);
        vaccination.setPet(pet);

        return vaccination;
    }

    private Sale sale(){
        Sale sale = TestUtils.sale();
        sale.setTotalSales(BigDecimal.valueOf(200));
        sale.setProfitSale(BigDecimal.valueOf(100));

        sale.setEmployee(employee);
        sale.setCustomer(customer);
        sale.getItemsSale().forEach(i -> {
            i.setProduct(product);
            i.setProductPrice(product.getCostPrice());
        });

        return sale;
    }

    private PetCare petCare(){
        PetCare petCare = TestUtils.petCare();
        petCare.setTotalRevenue(BigDecimal.valueOf(200.00));
        petCare.setTotalCostOperating(BigDecimal.valueOf(100.00));
        petCare.setTotalProfit(BigDecimal.valueOf(100.00));
        petCare.setCustomer(customer);
        petCare.setEmployee(employee);
        petCare.setPet(pet);

        return petCare;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnTotalBillingReport() throws Exception {
        PetCare petCare1 = petCare(); petCare1.setServiceDate(LocalDateTime.of(2025, 1, 1, 14, 30));
        PetCare petCare2 = petCare(); petCare2.setServiceDate(LocalDateTime.of(2025, 2, 2, 14, 30));
        petCareRepository.saveAll(List.of(petCare1, petCare2));

        Sale sale1 = sale(); sale1.setDateSale(LocalDate.of(2025, 1, 1));
        Sale sale2 = sale(); sale2.setDateSale(LocalDate.of(2025, 2, 2));
        saleRepository.saveAll(List.of(sale1, sale2));

        Vaccination vaccination1 = vaccination(); vaccination1.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination2 = vaccination(); vaccination2.setVaccinationDate(LocalDate.of(2025, 2, 2));
        vaccinationRepository.saveAll(List.of(vaccination1, vaccination2));

        mockMvc.perform(get("/dashboard/reports/by-period")
                        .param("dateMin", "2025-01-01")
                        .param("dateMax", "2025-02-03")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    Report report = objectMapper.readValue(json, Report.class);

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(1200.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(600.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(280.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(6, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnTotalBillingReportByPdf() throws Exception {
        PetCare petCare1 = petCare(); petCare1.setServiceDate(LocalDateTime.of(2025, 1, 1, 14, 30));
        PetCare petCare2 = petCare(); petCare2.setServiceDate(LocalDateTime.of(2025, 2, 2, 14, 30));
        petCareRepository.saveAll(List.of(petCare1, petCare2));

        Sale sale1 = sale(); sale1.setDateSale(LocalDate.of(2025, 1, 1));
        Sale sale2 = sale(); sale2.setDateSale(LocalDate.of(2025, 2, 2));
        saleRepository.saveAll(List.of(sale1, sale2));

        Vaccination vaccination1 = vaccination(); vaccination1.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination2 = vaccination(); vaccination2.setVaccinationDate(LocalDate.of(2025, 2, 2));
        vaccinationRepository.saveAll(List.of(vaccination1, vaccination2));

        mockMvc.perform(get("/dashboard/reports/by-pdf")
                        .param("dateMin", "2025-01-01")
                        .param("dateMax", "2025-02-03")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"));
    }

}
