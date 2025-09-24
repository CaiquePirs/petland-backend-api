package com.petland.product.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.product.controller.dto.ProductRequestDTO;
import com.petland.modules.product.controller.dto.ProductResponseDTO;
import com.petland.modules.product.controller.dto.ProductUpdateDTO;
import com.petland.modules.product.model.enums.ProductType;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class ProductIntegrationTest {

    @Autowired ProductRepository productRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void cleanBeforeTest(){
        productRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterTest(){
        productRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProductSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Shampoo dog clean")
                .productType(ProductType.PET_FOOD)
                .brand("Pet World LTDA")
                .costPrice(BigDecimal.valueOf(10.00))
                .costSale(BigDecimal.valueOf(20.00))
                .description("Shampoo Premium")
                .expirationDate(LocalDate.now().plusMonths(10))
                .manufactureDate(LocalDate.now().minusMonths(10))
                .stockQuantity(100)
                .supplierName("Pet World LTDA")
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ProductResponseDTO response = objectMapper.readValue(json, ProductResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.id()),
                            () -> assertEquals(dto.productType(), response.productType()),
                            () -> assertEquals(dto.brand(), response.brand()),
                            () -> assertEquals(dto.costPrice(), response.costPrice()),
                            () -> assertEquals(dto.costSale(), response.costSale()),
                            () -> assertEquals(dto.supplierName(), response.supplierName()),
                            () -> assertEquals(dto.description(), response.description()),
                            () -> assertEquals(dto.expirationDate(), response.expirationDate())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindProductIdSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ProductResponseDTO response = objectMapper.readValue(json, ProductResponseDTO.class);

                    assertAll(
                            () -> assertEquals(product.getId(), response.id()),
                            () -> assertEquals(product.getProductType(), response.productType()),
                            () -> assertEquals(product.getBrand(), response.brand()),
                            () -> assertThat(product.getCostPrice()).isEqualByComparingTo(response.costPrice()),
                            () -> assertThat(product.getCostSale()).isEqualByComparingTo(response.costSale()),
                            () -> assertEquals(product.getSupplierName(), response.supplierName()),
                            () -> assertEquals(product.getDescription(), response.description()),
                            () -> assertEquals(product.getExpirationDate(), response.expirationDate())
                    );
                });
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateProductIdSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        mockMvc.perform(delete("/products/" + product.getId())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        Product result = productRepository.findById(product.getId()).get();
        assertEquals(StatusEntity.DELETED, result.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllProductByFilterSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Product product1 = TestUtils.product(); product1.setEmployee(employee); product1.setStatus(StatusEntity.DELETED);
        Product product2 = TestUtils.product(); product2.setEmployee(employee);
        Product product3 = TestUtils.product(); product3.setEmployee(employee);
        Product product4 = TestUtils.product(); product4.setEmployee(employee);

        productRepository.saveAll(List.of(product1, product2, product3, product4));

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "ACTIVE")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Product product = TestUtils.product(); product.setEmployee(employee);
        productRepository.save(product);

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Shampoo Premium")
                .stockQuantity(10)
                .costSale(BigDecimal.valueOf(10))
                .costPrice(BigDecimal.valueOf(5))
                .brand("Pet World")
                .productType(ProductType.HYGIENE)
                .supplierName("Pet World")
                .description("Shampoo Premium")
                .expirationDate(LocalDate.now().plusMonths(6))
                .manufactureDate(LocalDate.now().minusMonths(6))
                .build();

        mockMvc.perform(put("/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
                .with(request -> {
                    request.setAttribute("id", product.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ProductResponseDTO response = objectMapper.readValue(json, ProductResponseDTO.class);

                    assertAll(
                            () -> assertEquals(product.getId(), response.id()),
                            () -> assertEquals(updateDTO.productType(), response.productType()),
                            () -> assertEquals(updateDTO.brand(), response.brand()),
                            () -> assertThat(updateDTO.costPrice()).isEqualByComparingTo(response.costPrice()),
                            () -> assertThat(updateDTO.costSale()).isEqualByComparingTo(response.costSale()),
                            () -> assertEquals(updateDTO.supplierName(), response.supplierName()),
                            () -> assertEquals(updateDTO.description(), response.description()),
                            () -> assertEquals(updateDTO.expirationDate(), response.expirationDate())
                    );
                });
    }


}
