package com.petland.dashboard.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DashboardSalesIntegrationTest {

    @Autowired private SaleRepository saleRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ObjectMapper objectMapper;

    private Customer customer;
    private Employee employee;
    private Product product;

    @BeforeEach
    void cleanBeforeTest(){
        saleRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();

        product = TestUtils.product();
        productRepository.save(product);

        customer = TestUtils.customer();
        customerRepository.save(customer);

        employee = TestUtils.employee();
        employeeRepository.save(employee);
    }

    @AfterEach
    void cleanAfterTest(){
        saleRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private Sale sale(){
        Sale sale = TestUtils.sale();

        sale.setEmployee(employee);
        sale.setCustomer(customer);
        sale.getItemsSale().forEach(i -> {
            i.setProduct(product);
            i.setProductPrice(product.getCostPrice());
        });

        return sale;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterSaleSuccessfully() throws Exception{
        Sale sale1 = sale(); sale1.setDateSale(LocalDate.of(2025, 1, 1));
        Sale sale2 = sale(); sale2.setDateSale(LocalDate.of(2025, 1, 1));
        Sale sale3 = sale(); sale3.setDateSale(LocalDate.of(2025, 2, 2));
        Sale sale4 = sale(); sale4.setDateSale(LocalDate.of(2025, 2, 2));

        saleRepository.saveAll(List.of(sale1, sale2, sale3, sale4));

        mockMvc.perform(get("/dashboard/reports/sales/by-period")
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

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(4, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnReportByProductSuccessfully() throws Exception{
        Sale sale1 = sale(); sale1.setCreateAt(LocalDateTime.of(2025, 1, 1, 14, 30));
        Sale sale2 = sale(); sale2.setCreateAt(LocalDateTime.of(2025, 1, 1, 14, 30));
        Sale sale3 = sale(); sale3.setCreateAt(LocalDateTime.of(2025, 2, 2, 14, 30));

        saleRepository.saveAll(List.of(sale1, sale2, sale3));

        UUID productId = product.getId();

        mockMvc.perform(get("/dashboard/reports/sales/by-product/" + productId)
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    Report report = objectMapper.readValue(json, Report.class);

                    assertThat(report.getTotalRevenue()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
                    assertThat(report.getTotalProfit()).isEqualByComparingTo(BigDecimal.valueOf(75.00));
                    assertThat(report.getOperatingCost()).isEqualByComparingTo(BigDecimal.valueOf(60.00));
                    assertEquals(employee.getId(), report.getEmployee().id());
                    assertEquals(3, report.getItemsQuantity());
                    assertNotNull(report.getIssueDate());
                });
    }



}
