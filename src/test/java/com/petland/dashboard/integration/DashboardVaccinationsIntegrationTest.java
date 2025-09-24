package com.petland.dashboard.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DashboardVaccinationsIntegrationTest {

    @Autowired private VaccinationRepository vaccinationRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private ObjectMapper objectMapper;

    private Employee employee;
    private Customer customer;
    private Pet pet;
    private Vaccine vaccine;

    @BeforeEach
    void cleanBeforeTest(){
        vaccinationRepository.deleteAll();
        vaccineRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();

        vaccine = TestUtils.vaccine();
        vaccineRepository.save(vaccine);

        employee = TestUtils.employee();
        employeeRepository.save(employee);

        customer = TestUtils.customer();
        pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

    }

    @AfterEach
    void cleanAfterTest(){
        vaccinationRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private Vaccination vaccination(){
        Vaccination vaccination = TestUtils.vaccination();
        vaccination.getAppliedVaccines().forEach(a -> a.setVaccine(vaccine));
        vaccination.setTotalByVaccination(BigDecimal.valueOf(300.00));
        vaccination.setProfitByVaccination(BigDecimal.valueOf(150.00));
        vaccination.setVeterinarian(employee);
        vaccination.setCustomer(customer);
        vaccination.setPet(pet);

        return vaccination;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnReportsByPeriodSuccessfully() throws Exception{
        Vaccination vaccination1 = vaccination(); vaccination1.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination2 = vaccination(); vaccination2.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination3 = vaccination(); vaccination3.setVaccinationDate(LocalDate.of(2025, 2, 2));
        Vaccination vaccination4 = vaccination(); vaccination4.setVaccinationDate(LocalDate.of(2025, 2, 2));

        vaccinationRepository.saveAll(List.of(vaccination1, vaccination2, vaccination3, vaccination4));

        mockMvc.perform(get("/dashboard/reports/vaccinations/by-period")
                .param("dateMin", "2025-01-01")
                .param("dateMax", "2025-02-02")
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
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(4, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnReportsByVaccineSuccessfully() throws Exception{
        Vaccination vaccination1 = vaccination(); vaccination1.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination2 = vaccination(); vaccination2.setVaccinationDate(LocalDate.of(2025, 1, 1));
        Vaccination vaccination3 = vaccination(); vaccination3.setVaccinationDate(LocalDate.of(2025, 2, 2));

        vaccinationRepository.saveAll(List.of(vaccination1, vaccination2, vaccination3));

        UUID vaccineId = vaccine.getId();

        mockMvc.perform(get("/dashboard/reports/vaccinations/by-vaccine/" + vaccineId)
                        .param("dateMin", "2025-01-01")
                        .param("dateMax", "2025-02-02")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    Report report = objectMapper.readValue(json, Report.class);

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(900.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(450.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(60.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(3, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }
}
