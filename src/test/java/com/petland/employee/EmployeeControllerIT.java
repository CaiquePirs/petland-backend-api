package com.petland.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class EmployeeControllerIT {

    @Autowired ObjectMapper objectMapper;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetEmployeeLoggedSuccessfully() throws Exception {
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        mockMvc.perform(get("/employees/profile")
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertAll(
                            () -> assertEquals(employee.getId(), response.id()),
                            () -> assertEquals(employee.getName(), response.name())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEmployeeByIdSuccessfully() throws Exception {
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        mockMvc.perform(get("/employees/" + employee.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);

                    assertAll(
                            () -> assertEquals(employee.getId(), response.id()),
                            () -> assertEquals(employee.getName(), response.name())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateEmployeeByIdSuccessfully() throws Exception {
        Employee employee = TestUtils.employee(); employee.setStatus(StatusEntity.ACTIVE);
        employeeRepository.save(employee);

        mockMvc.perform(delete("/employees/" + employee.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Optional<Employee> result = employeeRepository.findById(employee.getId());
        assertEquals(StatusEntity.DELETED, result.get().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllEmployeeByFilterSuccessfully() throws Exception {
        Employee employee1 = TestUtils.employee();employee1.setStatus(StatusEntity.DELETED);
        Employee employee2 = TestUtils.employee();
        Employee employee3 = TestUtils.employee();

        employeeRepository.saveAll(List.of(employee1, employee2, employee3));

        mockMvc.perform(get("/employees")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "ACTIVE")
                        .with(request -> {
                            request.setAttribute("id", UUID.randomUUID());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateEmployeeSuccessfully() throws Exception {
        Employee employee = TestUtils.employee();
        employeeRepository.saveAndFlush(employee);

        UUID employeeId = employee.getId();

        EmployeeUpdateDTO updaterDTO = EmployeeUpdateDTO.builder()
                .name("Employee Name")
                .phone("(000) 00 0000-0000")
                .email("employee@gmail.com")
                .department(Department.FINANCIAL)
                .dateBirth(LocalDate.now().minusYears(23))
                .hireDate(LocalDate.now().minusYears(5))
                .build();

        mockMvc.perform(put("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updaterDTO))
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    EmployeeResponseDTO response = objectMapper.readValue(json, EmployeeResponseDTO.class);

                    assertAll(
                            () -> assertEquals(employee.getId(), response.id()),
                            () -> assertNotEquals(employee.getEmail(), response.email()),
                            () -> assertNotEquals(employee.getName(), response.name()),
                            () -> assertNotEquals(employee.getPhone(), response.phone()),
                            () -> assertNotEquals(employee.getDateBirth(), response.dateBirth()),
                            () -> assertNotEquals(employee.getHireDate(), response.hireDate())
                    );
                });
    }
}
