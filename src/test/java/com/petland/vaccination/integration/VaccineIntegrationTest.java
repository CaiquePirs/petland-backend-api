package com.petland.vaccination.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.dto.VaccineUpdateDTO;
import com.petland.modules.vaccination.enums.VaccineType;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccineRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class VaccineIntegrationTest {

    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void cleanBeforeTest(){
        employee = TestUtils.employee();
        employeeRepository.save(employee);

        vaccineRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterTest(){
        vaccineRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterVaccineSuccessfully() throws Exception{
        VaccineRequestDTO dto = VaccineRequestDTO.builder()
                .lotNumber("L4345-5983TL")
                .vaccineType(VaccineType.FELINE_CALICIVIRUS)
                .priceSale(BigDecimal.valueOf(100.00))
                .purchasePrice(BigDecimal.valueOf(50.00))
                .supplierName("World Pet")
                .stockQuantity(100)
                .manufactureDate(LocalDate.now().minusMonths(5))
                .expirationDate(LocalDate.now().plusMonths(5))
                .build();

        mockMvc.perform(post("/vaccines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccineResponseDTO response = objectMapper.readValue(json, VaccineResponseDTO.class);

                    assertAll(
                            () -> assertEquals(dto.stockQuantity(), response.stockQuantity()),
                            () -> assertEquals(dto.vaccineType(), response.vaccineType()),
                            () -> assertEquals(dto.expirationDate(), response.expirationDate()),
                            () -> assertEquals(dto.lotNumber(), response.lotNumber()),
                            () -> assertEquals(dto.supplierName(), response.supplierName()),
                            () -> assertEquals(dto.purchasePrice(), response.purchasePrice()),
                            () -> assertEquals(dto.priceSale(), response.priceSale())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindVaccineByIdSuccessfully() throws Exception{
        Vaccine vaccine = TestUtils.vaccine();
        vaccineRepository.save(vaccine);

        mockMvc.perform(get("/vaccines/" + vaccine.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccineResponseDTO response = objectMapper.readValue(json, VaccineResponseDTO.class);

                    assertAll(
                            () -> assertEquals(vaccine.getStockQuantity(), response.stockQuantity()),
                            () -> assertEquals(vaccine.getVaccineType(), response.vaccineType()),
                            () -> assertThat(vaccine.getPriceSale()).isEqualByComparingTo(response.priceSale()),
                            () -> assertThat(vaccine.getPurchasePrice()).isEqualByComparingTo(response.purchasePrice()),
                            () -> assertEquals(vaccine.getExpirationDate(), response.expirationDate()),
                            () -> assertEquals(vaccine.getLotNumber(), response.lotNumber()),
                            () -> assertEquals(vaccine.getSupplierName(), response.supplierName()));
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateVaccineSuccessfully() throws Exception{
        Vaccine vaccine = TestUtils.vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);
        vaccineRepository.save(vaccine);

        mockMvc.perform(delete("/vaccines/" + vaccine.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Vaccine result = vaccineRepository.findById(vaccine.getId()).get();
        assertEquals(StatusEntity.DELETED, result.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllVaccinesByFilter() throws Exception{
        Vaccine vaccine1 = TestUtils.vaccine(); vaccine1.setStatus(StatusEntity.DELETED);
        Vaccine vaccine2 = TestUtils.vaccine();
        Vaccine vaccine3 = TestUtils.vaccine();
        Vaccine vaccine4 = TestUtils.vaccine();

        vaccineRepository.saveAll(List.of(vaccine1, vaccine2, vaccine3, vaccine4));

        mockMvc.perform(get("/vaccines")
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
    void shouldUpdateVaccineSuccessfully() throws Exception{
        Vaccine vaccine = TestUtils.vaccine();
        vaccineRepository.save(vaccine);

        VaccineUpdateDTO updateDTO = VaccineUpdateDTO.builder()
                .lotNumber("L4345-5983TL")
                .vaccineType(VaccineType.FELINE_CALICIVIRUS)
                .priceSale(BigDecimal.valueOf(100.00))
                .purchasePrice(BigDecimal.valueOf(50.00))
                .supplierName("World Pet")
                .stockQuantity(100)
                .manufactureDate(LocalDate.now().minusMonths(5))
                .expirationDate(LocalDate.now().plusMonths(5))
                .build();

        mockMvc.perform(put("/vaccines/" + vaccine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccineResponseDTO response = objectMapper.readValue(json, VaccineResponseDTO.class);

                    assertAll(
                            () -> assertEquals(vaccine.getId(), response.id()),
                            () -> assertEquals(updateDTO.stockQuantity(), response.stockQuantity()),
                            () -> assertEquals(updateDTO.vaccineType(), response.vaccineType()),
                            () -> assertThat(updateDTO.priceSale()).isEqualByComparingTo(response.priceSale()),
                            () -> assertThat(updateDTO.purchasePrice()).isEqualByComparingTo(response.purchasePrice()),
                            () -> assertEquals(updateDTO.expirationDate(), response.expirationDate()),
                            () -> assertEquals(updateDTO.lotNumber(), response.lotNumber()),
                            () -> assertEquals(updateDTO.supplierName(), response.supplierName()));
                });
    }
}
