package com.petland.sale.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class SaleIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SaleRepository saleRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private ProductRepository productRepository;

    private Employee employee;
    private Customer customer;

    @BeforeEach
    void cleanBeforeTest(){
        saleRepository.deleteAll();
        employeeRepository.deleteAll();
        customerRepository.deleteAll();

        customer = TestUtils.customer();
        customerRepository.save(customer);

        employee = TestUtils.employee();
        employeeRepository.save(employee);
    }

    @AfterEach
    void cleanAfterTest(){
        saleRepository.deleteAll();
        productRepository.deleteAll();
        employeeRepository.deleteAll();
        customerRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterSaleSuccessfully() throws Exception{
        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        ItemsSaleRequestDTO itemsSale = ItemsSaleRequestDTO.builder()
                .productQuantity(2)
                .productId(product.getId())
                .build();

        SaleRequestDTO dto = SaleRequestDTO.builder()
                .customerId(customer.getId())
                .paymentType(PaymentType.BANK_TRANSFER)
                .itemsSaleRequestDTO(List.of(itemsSale))
                .build();

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    SaleResponseDTO response = objectMapper.readValue(json, SaleResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.getId()),
                            () -> assertNotNull(response.getTotalSales()),
                            () -> assertNotNull(response.getItemsSaleResponseDTO()),
                            () -> assertEquals(customer.getId(), response.getCustomerId()),
                            () -> assertEquals(employee.getId(), response.getEmployeeId()),
                            () -> assertEquals(dto.paymentType(), response.getPaymentType())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindSaleByIdSuccessfully() throws Exception{
        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        Sale sale = TestUtils.sale();
        sale.getItemsSale().forEach(i -> i.setProduct(product));
        sale.setEmployee(employee);
        sale.setCustomer(customer);

        saleRepository.save(sale);

        mockMvc.perform(get("/sales/" + sale.getId())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    SaleResponseDTO response = objectMapper.readValue(json, SaleResponseDTO.class);

                    assertAll(
                            () -> assertEquals(sale.getId(), response.getId()),
                            () -> assertEquals(sale.getEmployee().getId(), response.getEmployeeId()),
                            () -> assertEquals(sale.getCustomer().getId(), response.getCustomerId())
                    );
                });
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateSaleByIdSuccessfully() throws Exception{
        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        Sale sale = TestUtils.sale();
        sale.getItemsSale().forEach(i -> i.setProduct(product));
        sale.setEmployee(employee);
        sale.setCustomer(customer);

        saleRepository.save(sale);

        mockMvc.perform(delete("/sales/" + sale.getId())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        Sale result = saleRepository.findById(sale.getId()).get();

        assertEquals(StatusEntity.DELETED, result.getStatus());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllSalesByFilterSuccessfully() throws Exception{
        Product product = TestUtils.product(); product.setEmployee(employee);
        productRepository.save(product);

        Sale sale1 = TestUtils.sale(); sale1.setEmployee(employee); sale1.setCustomer(customer); sale1.setStatus(StatusEntity.DELETED);
        Sale sale2 = TestUtils.sale(); sale2.setEmployee(employee); sale2.setCustomer(customer);
        Sale sale3 = TestUtils.sale(); sale3.setEmployee(employee); sale3.setCustomer(customer);

        Stream.of(sale1, sale2, sale3).flatMap(sale -> sale.getItemsSale().stream())
                .toList()
                .forEach(i -> i.setProduct(product));

        saleRepository.saveAll(List.of(sale1, sale2, sale3));

        mockMvc.perform(get("/sales")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "ACTIVE")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllSalesByCustomerIdSuccessfully() throws Exception{
        Product product = TestUtils.product(); product.setEmployee(employee);
        productRepository.save(product);

        Sale sale1 = TestUtils.sale(); sale1.setEmployee(employee); sale1.setCustomer(customer);
        Sale sale2 = TestUtils.sale(); sale2.setEmployee(employee); sale2.setCustomer(customer);
        Sale sale3 = TestUtils.sale(); sale3.setEmployee(employee); sale3.setCustomer(customer);

        Stream.of(sale1, sale2, sale3).flatMap(sale -> sale.getItemsSale().stream())
                .toList()
                .forEach(i -> i.setProduct(product));

        saleRepository.saveAll(List.of(sale1, sale2, sale3));

        mockMvc.perform(get("/sales/" + customer.getId() + "/customer")
                        .param("page", "0")
                        .param("size", "10")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }




}
