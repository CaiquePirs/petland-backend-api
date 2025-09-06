package com.petland.petCare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetCareIntegrationTest {

    @Autowired private PetCareRepository petCareRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void cleanBeforeTest(){
        petCareRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterTest(){
        petCareRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterPetCareServiceSuccessfully() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet(); pet.setOwner(customer); customer.getMyPets().add(pet);
        customerRepository.save(customer);

        PetCareRequestDTO dto = PetCareRequestDTO.builder()
                .employeeId(employee.getId())
                .customerId(customer.getId())
                .petId(pet.getId())
                .serviceDetailsList(List.of(PetCareDetailsRequestDTO.builder()
                        .petCareType(PetCareType.EarCleaning)
                        .quantityService(2)
                        .operatingCost(BigDecimal.valueOf(10.00))
                        .unitPrice(BigDecimal.valueOf(20.00))
                        .noteService("Notes")
                        .build()))
                .location(TestUtils.address())
                .build();

        mockMvc.perform(post("/petcare/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                    request.setAttribute("id", UUID.randomUUID());
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetCareResponseDTO response = objectMapper.readValue(json, PetCareResponseDTO.class);

                    assertAll(
                            () -> assertEquals(dto.petId(), response.petId()),
                            () -> assertEquals(dto.customerId(), response.customerId()),
                            () -> assertEquals(dto.employeeId(), response.employeeId()),
                            () -> assertNotNull(response.totalProfit()),
                            () -> assertNotNull(response.totalCostOperating()),
                            () -> assertNotNull(response.totalRevenue()),
                            () -> assertNotNull(response.location()));
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindPetCareServiceByIdSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet(); customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        PetCare petCare = TestUtils.petCare();
        petCare.setEmployee(employee); petCare.setCustomer(customer); petCare.setPet(pet);
        petCareRepository.save(petCare);

        mockMvc.perform(get("/petcare/services/" + petCare.getId())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetCareResponseDTO response = objectMapper.readValue(json, PetCareResponseDTO.class);

                    assertAll(
                            () -> assertEquals(petCare.getId(), response.id()),
                            () -> assertEquals(petCare.getPet().getId(), response.petId()),
                            () -> assertEquals(petCare.getCustomer().getId(), response.customerId()),
                            () -> assertEquals(petCare.getEmployee().getId(), response.employeeId()));
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivatePetCareServiceSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet(); customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        PetCare petCare = TestUtils.petCare();
        petCare.setStatus(StatusEntity.ACTIVE);
        petCare.setEmployee(employee); petCare.setCustomer(customer); petCare.setPet(pet);
        petCareRepository.save(petCare);

        mockMvc.perform(delete("/petcare/services/" + petCare.getId())
                .with(request -> {
                    request.setAttribute("id", petCare.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        PetCare result = petCareRepository.findById(petCare.getId()).get();
        assertEquals(StatusEntity.DELETED, result.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAllPetCareServiceSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet(); customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        PetCare petCare1 = TestUtils.petCare(); petCare1.setEmployee(employee); petCare1.setCustomer(customer); petCare1.setPet(pet);
        PetCare petCare2 = TestUtils.petCare(); petCare2.setEmployee(employee); petCare2.setCustomer(customer); petCare2.setPet(pet);
        PetCare petCare3 = TestUtils.petCare(); petCare3.setEmployee(employee); petCare3.setCustomer(customer); petCare3.setPet(pet);
        petCare1.setStatus(StatusEntity.DELETED);

        petCareRepository.saveAll(List.of(petCare1, petCare2, petCare3));

        mockMvc.perform(get("/petcare/services")
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
}
