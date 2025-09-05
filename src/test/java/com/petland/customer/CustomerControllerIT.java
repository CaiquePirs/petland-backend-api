package com.petland.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.dto.UpdateCustomerDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class CustomerControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomerRepository customerRepository;
    @Autowired EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        customerRepository.deleteAll();
        employeeRepository.deleteAll();

        employee = TestUtils.employee();
        employeeRepository.save(employee);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindCustomerByIdSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        UUID customerId = customer.getId();

        mockMvc.perform(get("/customers/" + customerId)
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertAll(
                            () -> assertEquals(customerId, response.id()),
                            () -> assertEquals(customer.getName(), response.name())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateCustomerSuccessfullyByEmployee() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        UUID customerId = customer.getId();

        mockMvc.perform(delete("/customers/" + customerId)
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        Optional<Customer> result = customerRepository.findById(customerId);
        assertEquals(StatusEntity.DELETED, result.get().getStatus());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldDeactivateCustomerSuccessfullyByCustomer() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        UUID customerId = customer.getId();

        mockMvc.perform(delete("/customers/" + customerId)
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Optional<Customer> result = customerRepository.findById(customerId);
        assertEquals(StatusEntity.DELETED, result.get().getStatus());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Should throw exception when trying to deactivate customer without ownership")
    void shouldThrowIfNotOwner() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        UUID customerId = customer.getId();
        UUID userUnauthorizedId = UUID.randomUUID();

        mockMvc.perform(delete("/customers/" + customerId)
                .with(request -> {
                    request.setAttribute("id", userUnauthorizedId);
                    return request;
                }))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(UnauthorizedException.class, result.getResolvedException()),
                        () -> assertEquals("User not authorized", result.getResolvedException().getMessage()),
                        () -> assertNotEquals(userUnauthorizedId, customer.getId())
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllCustomerByFilterSuccessfully() throws Exception{
        Customer customer1 = TestUtils.customer(); customer1.setStatus(StatusEntity.DELETED);
        Customer customer2 = TestUtils.customer();
        Customer customer3 = TestUtils.customer();

        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        mockMvc.perform(get("/customers")
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
    void shouldUpdateCustomerSuccessfullyByEmployee() throws Exception {
        Customer customer = TestUtils.customer();
        customerRepository.saveAndFlush(customer);

        UUID customerId = customer.getId();

        UpdateCustomerDTO updaterDTO = UpdateCustomerDTO.builder()
                .name("Customer Name")
                .phone("(000) 00 0000-0000")
                .email("customer@gmail.com")
                .dateBirth(LocalDate.now().minusYears(21))
                .build();

        mockMvc.perform(put("/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updaterDTO))
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertNotEquals(customer.getEmail(), response.email());
                    assertEquals(customer.getId(), response.id());
                    assertEquals(updaterDTO.name(), response.name());
                    assertEquals(updaterDTO.phone(), response.phone());
                    assertEquals(updaterDTO.email(), response.email());
                    assertEquals(updaterDTO.dateBirth(), response.dateBirth());
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldUpdateCustomerSuccessfullyByCustomer() throws Exception {
        Customer customer = TestUtils.customer();
        customerRepository.saveAndFlush(customer);

        UUID customerId = customer.getId();

        UpdateCustomerDTO updaterDTO = UpdateCustomerDTO.builder()
                .name("Customer Name")
                .phone("(000) 00 0000-0000")
                .email("customer@gmail.com")
                .dateBirth(LocalDate.now().minusYears(21))
                .build();

        mockMvc.perform(put("/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updaterDTO))
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertNotEquals(customer.getEmail(), response.email());
                    assertEquals(customer.getId(), response.id());
                    assertEquals(updaterDTO.name(), response.name());
                    assertEquals(updaterDTO.phone(), response.phone());
                    assertEquals(updaterDTO.email(), response.email());
                    assertEquals(updaterDTO.dateBirth(), response.dateBirth());
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Should throw exception when trying to update customer without ownership")
    void shouldThrowExceptionIfNotOwner() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.saveAndFlush(customer);

        UUID customerId = customer.getId();
        UUID userUnauthorizedId = UUID.randomUUID();

        UpdateCustomerDTO updaterDTO = UpdateCustomerDTO.builder()
                .name("Customer Name")
                .phone("(000) 00 0000-0000")
                .email("customer@gmail.com")
                .dateBirth(LocalDate.now().minusYears(21))
                .build();

        mockMvc.perform(put("/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updaterDTO))
                .with(request -> {
                    request.setAttribute("id", userUnauthorizedId);
                    return request;
                }))
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(UnauthorizedException.class, result.getResolvedException()),
                        () -> assertEquals("User not authorized", result.getResolvedException().getMessage()),
                        () -> assertNotEquals(userUnauthorizedId, customer.getId())
                ));
    }
}
