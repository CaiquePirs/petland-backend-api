package com.petland.employee.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.exception.EmailFoundException;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.modules.employee.controller.dto.EmployeeRequestDTO;
import com.petland.modules.employee.controller.dto.EmployeeResponseDTO;
import com.petland.modules.employee.model.enums.Department;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(value = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthEmployeeIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void cleanBeforeTest(){
        employeeRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterTest(){
        employeeRepository.deleteAll();
    }

    @Test
    void shouldEmployeeSuccessfullyLogin() throws Exception{
        Employee employee = TestUtils.employee();
        String password = "Employee@1234456";
        employee.setPassword(passwordEncoder.encode(password));
        employeeRepository.save(employee);

        AuthRequestDTO request = new AuthRequestDTO(employee.getEmail(), password);

        mockMvc.perform(post("/auth/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenLoginPasswordNotMatch() throws Exception{
        Employee employee = TestUtils.employee();
        String password = "Employee@1234456";
        employee.setPassword(passwordEncoder.encode(password));
        employeeRepository.save(employee);

        AuthRequestDTO request = new AuthRequestDTO(employee.getEmail(), "12345678");

        mockMvc.perform(post("/auth/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(InvalidCredentialsException.class, result.getResolvedException()),
                        () -> assertEquals("Email/password incorrect", result.getResolvedException().getMessage()))
                );
        assertFalse(passwordEncoder.matches(employee.getPassword(), "12345678"));
    }

    @Test
    void shouldThrowExceptionWhenEmailEmployeeIsIncorrect() throws Exception{
        String password = "Employee@1234456";
        String email = "employee@gmail.com";

        Employee employee = TestUtils.employee();
        employee.setPassword(passwordEncoder.encode(password));
        employeeRepository.save(employee);

        AuthRequestDTO request = new AuthRequestDTO(email, password);

        mockMvc.perform(post("/auth/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(InvalidCredentialsException.class, result.getResolvedException()),
                        () -> assertEquals("Email/password incorrect", result.getResolvedException().getMessage()),
                        () -> assertNotEquals(request.email(), employee.getEmail())
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterEmployeeSuccessfully() throws Exception{
        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .name("Employee")
                .phone("(000) 00 0000-0000")
                .email("employee@gmail.com")
                .address(TestUtils.address())
                .dateBirth(LocalDate.now().minusYears(18))
                .password("Employee@123456789")
                .hireDate(LocalDate.now().minusYears(6))
                .department(Department.ATTENDANT)
                .build();

        mockMvc.perform(post("/auth/employees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(servlet -> {
                            servlet.setAttribute("id", UUID.randomUUID());
                            return servlet;
                        }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    EmployeeResponseDTO response = objectMapper.readValue(json, EmployeeResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.id()),
                            () -> assertEquals(request.email(), response.email()),
                            () -> assertEquals(request.phone(), response.phone()),
                            () -> assertEquals(request.name(), response.name())
                    );
                });
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowExceptionWhenCustomerEmailAlreadyExist() throws Exception{
        Employee employee = TestUtils.employee();
        employee.setEmail("employee@gmail.com");
        employeeRepository.save(employee);

        EmployeeRequestDTO request = EmployeeRequestDTO.builder()
                .name("Employee")
                .phone("(000) 00 0000-0000")
                .email("employee@gmail.com")
                .address(TestUtils.address())
                .dateBirth(LocalDate.now().minusYears(18))
                .password("Employee@123456789")
                .hireDate(LocalDate.now().minusYears(6))
                .department(Department.ATTENDANT)
                .build();

        mockMvc.perform(post("/auth/employees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(servlet -> {
                            servlet.setAttribute("id", UUID.randomUUID());
                            return servlet;
                        }))
                .andExpect(status().isConflict())
                .andExpect(result -> assertAll(
                        () -> assertInstanceOf(EmailFoundException.class, result.getResolvedException()),
                        () -> assertEquals("This email already exists", result.getResolvedException().getMessage())
                ));
    }

}
