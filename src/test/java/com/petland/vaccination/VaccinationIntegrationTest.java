package com.petland.vaccination;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.dto.UpdateAddressDTO;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccinationRepository;
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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class VaccinationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private VaccinationRepository vaccinationRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetRepository petRepository;

    private Customer customer;
    private Pet pet;
    private Employee employee;
    private Vaccine vaccine;

    @BeforeEach
    void cleanBeforeEach(){
        vaccinationRepository.deleteAll();
        vaccineRepository.deleteAll();
        employeeRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();

        vaccine = TestUtils.vaccine();
        vaccineRepository.save(vaccine);

        employee = TestUtils.employee();
        employeeRepository.save(employee);

        pet = TestUtils.pet();
        customer = TestUtils.customer();
        customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);
    }

    @AfterEach
    void cleanAfterTest(){
        vaccinationRepository.deleteAll();
        vaccineRepository.deleteAll();
        employeeRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private Vaccination vaccination(){
        Vaccination vaccination = TestUtils.vaccination();
        vaccination.setCustomer(customer);
        vaccination.setVeterinarian(employee);
        vaccination.setPet(pet);
        vaccination.getAppliedVaccines().forEach(a -> a.setVaccine(vaccine));

        return vaccination;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterVaccinationsSuccessfully() throws Exception{
        AppliedVaccineRequestDTO appliedRequest = AppliedVaccineRequestDTO.builder()
                .quantityUsed(2)
                .vaccineId(vaccine.getId())
                .build();

        VaccinationRequestDTO dto = VaccinationRequestDTO.builder()
                .veterinarianId(employee.getId())
                .petId(pet.getId())
                .customerId(customer.getId())
                .vaccinationDate(LocalDate.now())
                .nextDoseDate(LocalDate.now().plusMonths(5))
                .clinicalNotes("Notes")
                .location(TestUtils.address())
                .listAppliedVaccineRequestDTO(List.of(appliedRequest))
                .build();

        mockMvc.perform(post("/vaccinations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccinationResponseDTO response = objectMapper.readValue(json, VaccinationResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.getId()),
                            () -> assertNotNull(response.getListAppliedVaccineResponseDTO()),
                            () -> assertNotNull(response.getLocation()),
                            () -> assertEquals(dto.customerId(), response.getCustomerId()),
                            () -> assertEquals(dto.petId(), response.getPetId()),
                            () -> assertEquals(dto.veterinarianId(), response.getVeterinarianId()),
                            () -> assertEquals(dto.clinicalNotes(), response.getClinicalNotes()),
                            () -> assertEquals(dto.nextDoseDate(), response.getNextDoseDate()),
                            () -> assertEquals(dto.vaccinationDate(), response.getVaccinationDate())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindVaccinationByIdSuccessfully() throws Exception{
        Vaccination vaccination = vaccination();
        vaccinationRepository.save(vaccination);

        mockMvc.perform(get("/vaccinations/" + vaccination.getId())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccinationResponseDTO response = objectMapper.readValue(json, VaccinationResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(response.getId()),
                            () -> assertNotNull(response.getListAppliedVaccineResponseDTO()),
                            () -> assertNotNull(response.getLocation()),
                            () -> assertEquals(vaccination.getCustomer().getId(), response.getCustomerId()),
                            () -> assertEquals(vaccination.getPet().getId(), response.getPetId()),
                            () -> assertEquals(vaccination.getVeterinarian().getId(), response.getVeterinarianId()),
                            () -> assertEquals(vaccination.getClinicalNotes(), response.getClinicalNotes()),
                            () -> assertEquals(vaccination.getNextDoseDate(), response.getNextDoseDate()),
                            () -> assertEquals(vaccination.getVaccinationDate(), response.getVaccinationDate())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateVaccinationSuccessfully() throws Exception{
        Vaccination vaccination = vaccination();
        vaccinationRepository.save(vaccination);

        mockMvc.perform(delete("/vaccinations/" + vaccination.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Vaccination result = vaccinationRepository.findById(vaccination.getId()).get();
        assertNotEquals(vaccination.getStatus(), result.getStatus());
        assertEquals(StatusEntity.DELETED, result.getStatus());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateVaccinationSuccessfully() throws Exception{
        Vaccination vaccination = vaccination();
        vaccinationRepository.save(vaccination);

        UpdateAddressDTO addressDTO = UpdateAddressDTO.builder()
                .city("Salvador")
                .state("Bahia")
                .country("Brazil")
                .number("20")
                .zipCode("000-000-00")
                .street("New Street")
                .build();

        VaccinationUpdateDTO updateDTO = VaccinationUpdateDTO.builder()
                .clinicalNotes("Notes Vaccinations")
                .vaccinationDate(LocalDate.now().minusYears(1))
                .nextDoseDate(LocalDate.now().plusMonths(6))
                .location(addressDTO)
                .build();

        mockMvc.perform(put("/vaccinations/" + vaccination.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    VaccinationResponseDTO response = objectMapper.readValue(json, VaccinationResponseDTO.class);

                    assertAll(
                            () -> assertEquals(addressDTO.city(), response.getLocation().getCity()),
                            () -> assertEquals(addressDTO.country(), response.getLocation().getCountry()),
                            () -> assertEquals(addressDTO.street(), response.getLocation().getStreet()),
                            () -> assertEquals(addressDTO.zipCode(), response.getLocation().getZipCode()),
                            () -> assertEquals(addressDTO.state(), response.getLocation().getState()),
                            () -> assertEquals(addressDTO.number(), response.getLocation().getNumber())
                    );

                    assertAll(
                            () -> assertEquals(vaccination.getId(), response.getId()),
                            () -> assertEquals(updateDTO.clinicalNotes(), response.getClinicalNotes()),
                            () -> assertEquals(updateDTO.vaccinationDate(), response.getVaccinationDate()),
                            () -> assertEquals(updateDTO.nextDoseDate(), response.getNextDoseDate())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllVaccinationsByFilter() throws Exception{
        Vaccination vaccination1 = vaccination(); vaccination1.setStatus(StatusEntity.DELETED);
        Vaccination vaccination2 = vaccination();
        Vaccination vaccination3 = vaccination();

        vaccinationRepository.saveAll(List.of(vaccination1, vaccination2, vaccination3));

        mockMvc.perform(get("/vaccinations")
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
