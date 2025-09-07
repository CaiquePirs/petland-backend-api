package com.petland.dashboard.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.repositories.PetCareRepository;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DashboardPetCareIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PetCareRepository petCareRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;

    private Employee employee;
    private Customer customer;
    private Pet pet;

    @BeforeEach
    void cleanBeforeTest(){
        petCareRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();

        employee = TestUtils.employee();
        employeeRepository.save(employee);

        customer = TestUtils.customer();
        pet = TestUtils.pet(); pet.setOwner(customer); customer.getMyPets().add(pet);
        customerRepository.save(customer);
    }

    @AfterEach
    void cleanAfterTest(){
        petCareRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private PetCare petCare(){
        PetCare petCare = TestUtils.petCare();
        petCare.setTotalRevenue(BigDecimal.valueOf(200.00));
        petCare.setTotalCostOperating(BigDecimal.valueOf(50.00));
        petCare.setTotalProfit(BigDecimal.valueOf(150.00));
        petCare.setCustomer(customer);
        petCare.setEmployee(employee);
        petCare.setPet(pet);

        return petCare;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnServiceReportByPeriodSuccessfully() throws Exception{
        PetCare petCare1 = petCare(); petCare1.setServiceDate(LocalDateTime.of(2025, 1, 1, 14, 30));
        PetCare petCare2 = petCare(); petCare2.setServiceDate(LocalDateTime.of(2025, 1, 1, 14, 30));
        PetCare petCare3 = petCare(); petCare3.setServiceDate(LocalDateTime.of(2025, 2, 2, 14, 30));
        PetCare petCare4 = petCare(); petCare4.setServiceDate(LocalDateTime.of(2025, 2, 2, 14, 30));

        petCareRepository.saveAll(List.of(petCare1, petCare2, petCare3, petCare4));

        mockMvc.perform(get("/dashboard/reports/pet-care/by-period")
                        .param("page", "0")
                        .param("size", "10")
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

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(800.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(600.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(4, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnServiceReportByPetCareTypeSuccessfully() throws Exception{
        PetCare petCare1 = petCare(); petCare1.getPetCareDetails().forEach(d -> d.setPetCareType(PetCareType.GROOMING));
        PetCare petCare2 = petCare(); petCare2.getPetCareDetails().forEach(d -> d.setPetCareType(PetCareType.CHECKUP));
        PetCare petCare3 = petCare(); petCare3.getPetCareDetails().forEach(d -> d.setPetCareType(PetCareType.CHECKUP));
        PetCare petCare4 = petCare(); petCare4.getPetCareDetails().forEach(d -> d.setPetCareType(PetCareType.CHECKUP));

        petCareRepository.saveAll(List.of(petCare1, petCare2, petCare3, petCare4));

        mockMvc.perform(get("/dashboard/reports/pet-care/by-service-type")
                        .param("type", "CHECKUP")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    Report report = objectMapper.readValue(json, Report.class);

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(600.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(450.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(3, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }
}
