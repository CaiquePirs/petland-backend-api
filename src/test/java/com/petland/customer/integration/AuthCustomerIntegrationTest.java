package com.petland.customer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.exception.EmailFoundException;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthCustomerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired CustomerRepository customerRepository;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void cleanBeforeTest(){
        customerRepository.deleteAll();
    }

    @Test
    void shouldCustomerSuccessfullyLogin() throws Exception{
        Customer customer = TestUtils.customer();
        String password = "Customer@1234456";
        customer.setPassword(passwordEncoder.encode(password));
        customerRepository.save(customer);

        AuthRequestDTO request = new AuthRequestDTO(customer.getEmail(), password);

        mockMvc.perform(post("/auth/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenLoginPasswordNotMatch() throws Exception{
        Customer customer = TestUtils.customer();
        String password = "Customer@1234456";
        customer.setPassword(passwordEncoder.encode(password));
        customerRepository.save(customer);

        AuthRequestDTO request = new AuthRequestDTO(customer.getEmail(), "12345678");

        mockMvc.perform(post("/auth/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(InvalidCredentialsException.class, result.getResolvedException()),
                        () -> assertEquals("Email/password incorrect", result.getResolvedException().getMessage()))
                );
        assertFalse(passwordEncoder.matches(customer.getPassword(), "12345678"));
    }

    @Test
    void shouldThrowExceptionWhenEmailCustomerIsIncorrect() throws Exception{
        String password = "Customer@1234456";
        String email = "customer@gmail.com";

        Customer customer = TestUtils.customer();
        customer.setPassword(passwordEncoder.encode(password));
        customerRepository.save(customer);

        AuthRequestDTO request = new AuthRequestDTO(email, password);

        mockMvc.perform(post("/auth/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(InvalidCredentialsException.class, result.getResolvedException()),
                        () -> assertEquals("Email/password incorrect", result.getResolvedException().getMessage()),
                        () -> assertNotEquals(request.email(), customer.getEmail())
                ));
    }

    @Test
    void shouldRegisterCustomerSuccessfully() throws Exception{
        customerRepository.deleteAll();

        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .name("Customer")
                .phone("(000) 00 0000-0000")
                .email("customer@gmail.com")
                .address(TestUtils.address())
                .dateBirth(LocalDate.now().minusYears(18))
                .password("Customer@123456789")
                .build();

        mockMvc.perform(post("/auth/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.id()),
                            () -> assertEquals(request.email(), response.email()),
                            () -> assertEquals(request.phone(), response.phone()),
                            () -> assertEquals(request.name(), response.name())
                    );
                });
    }


    @Test
    void shouldThrowExceptionWhenCustomerEmailAlreadyExist() throws Exception{
        Customer customer = TestUtils.customer();
        customer.setEmail("customer@gmail.com");
        customerRepository.save(customer);

        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .name("Customer")
                .phone("(000) 00 0000-0000")
                .email("customer@gmail.com")
                .address(TestUtils.address())
                .dateBirth(LocalDate.now().minusYears(18))
                .password("Customer@123456789")
                .build();

        mockMvc.perform(post("/auth/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(EmailFoundException.class, result.getResolvedException()),
                        () -> assertEquals("This email already exists", result.getResolvedException().getMessage())
                ));
    }
}
