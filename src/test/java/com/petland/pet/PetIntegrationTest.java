package com.petland.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.dto.PetUpdateDTO;
import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomerRepository customerRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired PetRepository petRepository;

    @BeforeEach
    void cleanBeforeTest(){
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterAll(){
        petRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldRegisterPetSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        PetRequestDTO requestDTO = PetRequestDTO.builder()
                .name("Bob")
                .age(2)
                .breed("Dog")
                .gender(PetGender.MALE)
                .weight(0.03)
                .dateBirth(LocalDate.now().minusYears(1))
                .specie(PetSpecies.DOG)
                .customerId(customer.getId())
                .build();

        mockMvc.perform(post("/pets")
                .with(request -> {
                    request.setAttribute("id", customer.getId());
                    return request;
                })
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetResponseDTO response = objectMapper.readValue(json, PetResponseDTO.class);

                    assertAll(
                            () -> assertEquals(requestDTO.age(), response.age()),
                            () -> assertEquals(requestDTO.breed(), response.breed()),
                            () -> assertEquals(requestDTO.name(), response.name()),
                            () -> assertEquals(requestDTO.specie(), response.specie()),
                            () -> assertEquals(requestDTO.weight(), response.weight())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldFindPetByIdSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);

        customerRepository.save(customer);

        mockMvc.perform(get("/pets/" + pet.getId())
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetResponseDTO response = objectMapper.readValue(json, PetResponseDTO.class);

                    assertAll(
                            () -> assertEquals(pet.getId(), response.id()),
                            () -> assertEquals(pet.getName(), response.name())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindPetByIdSuccessfullyByEmployee() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);

        customerRepository.save(customer);

        mockMvc.perform(get("/pets/" + pet.getId())
                        .with(request -> {
                            request.setAttribute("id", UUID.randomUUID());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetResponseDTO response = objectMapper.readValue(json, PetResponseDTO.class);

                    assertAll(
                            () -> assertEquals(pet.getId(), response.id()),
                            () -> assertEquals(pet.getName(), response.name())
                    );
                });
    }


    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Should throw exception when user tries to find pet by ID without being the owner or an ADMIN")
    void shouldThrowExceptionWhenIsNotPetOwner() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);

        customerRepository.save(customer);

        mockMvc.perform(get("/pets/" + pet.getId())
                        .with(request -> {
                            request.setAttribute("id", UUID.randomUUID());
                            return request;
                        }))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertInstanceOf(UnauthorizedException.class, result.getResolvedException()));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldDeactivatePetSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        pet.setStatus(StatusEntity.ACTIVE); customer.getMyPets().add(pet); pet.setOwner(customer);

        customerRepository.save(customer);

        mockMvc.perform(delete("/pets/" + pet.getId())
                .with(request -> {
                    request.setAttribute("id", customer.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        Pet result = petRepository.findById(pet.getId()).get();
        assertEquals(StatusEntity.DELETED, result.getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllPetsByFilterSuccessfully() throws Exception{
        Customer customer = TestUtils.customer();

        Pet pet1 = TestUtils.pet(); pet1.setStatus(StatusEntity.ACTIVE); pet1.setOwner(customer);
        Pet pet2 = TestUtils.pet(); pet2.setStatus(StatusEntity.ACTIVE); pet2.setOwner(customer);
        Pet pet3 = TestUtils.pet(); pet3.setStatus(StatusEntity.ACTIVE); pet3.setOwner(customer);
        Pet pet4 = TestUtils.pet(); pet4.setStatus(StatusEntity.DELETED); pet4.setOwner(customer);

        customer.setMyPets(List.of(pet1, pet2, pet3, pet4));
        customerRepository.save(customer);

        mockMvc.perform(get("/pets")
                .with(request -> {
                    request.setAttribute("id", UUID.randomUUID());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldUpdatePetSuccessfully() throws Exception {
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        pet.setStatus(StatusEntity.ACTIVE); customer.getMyPets().add(pet); pet.setOwner(customer);

        customerRepository.save(customer);

        PetUpdateDTO updateDTO = PetUpdateDTO.builder()
                .name("Nina")
                .age(3)
                .breed("Cat")
                .gender(PetGender.FEMALE)
                .weight(0.04)
                .dateBirth(LocalDate.now().minusYears(2))
                .specie(PetSpecies.CAT)
                .build();

        mockMvc.perform(put("/pets/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PetResponseDTO response = objectMapper.readValue(json, PetResponseDTO.class);

                    assertAll(
                            () -> assertEquals(pet.getId(), response.id()),
                            () -> assertEquals(updateDTO.name(), response.name()),
                            () -> assertEquals(updateDTO.breed(), response.breed()),
                            () -> assertEquals(updateDTO.age(), response.age()),
                            () -> assertEquals(updateDTO.dateBirth(), response.dateBirth()),
                            () -> assertEquals(updateDTO.weight(), response.weight()),
                            () -> assertEquals(updateDTO.gender(), response.gender()),
                            () -> assertEquals(updateDTO.specie(), response.specie()));
                });
    }
}
