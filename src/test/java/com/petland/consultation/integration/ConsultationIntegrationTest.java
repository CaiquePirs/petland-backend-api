package com.petland.consultation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ConsultationStatus;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.consultation.repositories.ConsultationRepository;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccineRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsultationIntegrationTest {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ConsultationRepository consultationRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private Employee employee;
    private Customer customer;
    private Pet pet;
    private Vaccine vaccine;
    private Product product;

    @BeforeEach
    void cleanBeforeTest() {
        customerRepository.deleteAll();
        petRepository.deleteAll();
        productRepository.deleteAll();

        employee = TestUtils.employee();
        employeeRepository.save(employee);

        customer = TestUtils.customer();
        pet = TestUtils.pet();
        customer.getMyPets().add(pet);
        pet.setOwner(customer);
        customerRepository.save(customer);

        vaccine = TestUtils.vaccine();
        product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);
        vaccineRepository.save(vaccine);
    }

    @AfterEach
    void cleanAfterTest(){
        consultationRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
        vaccineRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private SaleRequestDTO saleRequest(){
        ItemsSaleRequestDTO itemRequest = ItemsSaleRequestDTO.builder()
                .productId(product.getId())
                .productQuantity(2)
                .build();

        return SaleRequestDTO.builder()
                .itemsSaleRequestDTO(List.of(itemRequest))
                .paymentType(PaymentType.BANK_TRANSFER)
                .customerId(customer.getId())
                .build();

    }

    private VaccinationRequestDTO vaccinationRequest(){
        AppliedVaccineRequestDTO appliedRequest = AppliedVaccineRequestDTO.builder()
            .vaccineId(vaccine.getId())
            .quantityUsed(2)
            .build();

        return VaccinationRequestDTO.builder()
                .listAppliedVaccineRequestDTO(List.of(appliedRequest))
                .location(TestUtils.address())
                .vaccinationDate(LocalDate.now())
                .nextDoseDate(LocalDate.now().plusMonths(5))
                .clinicalNotes("Notes")
                .veterinarianId(employee.getId())
                .customerId(customer.getId())
                .petId(pet.getId())
                .build();
    }

    private PetCareRequestDTO petCareRequest(){
        PetCareDetailsRequestDTO detailsRequest = PetCareDetailsRequestDTO.builder()
                .unitPrice(BigDecimal.valueOf(30))
                .quantityService(2)
                .operatingCost(BigDecimal.valueOf(20))
                .noteService("Notes")
                .petCareType(PetCareType.BATH)
                .build();

        return PetCareRequestDTO.builder()
                .location(TestUtils.address())
                .petId(pet.getId())
                .customerId(customer.getId())
                .employeeId(employee.getId())
                .serviceDetailsList(List.of(detailsRequest))
                .build();
    }

    private Consultation consultation(){
        return Consultation.builder()
                .details(ConsultationDetails.builder()
                        .location(TestUtils.address())
                        .consultationStatus(ConsultationStatus.OPEN)
                        .paymentType(PaymentType.BANK_TRANSFER)
                        .priority(ConsultationPriority.HIGH)
                        .type(ServiceType.LAB_TEST)
                        .serviceDate(LocalDateTime.now().minusDays(5))
                        .notes("Notes")
                        .totalByService(BigDecimal.valueOf(100))
                        .costOperatingByService(BigDecimal.valueOf(50))
                        .profitByService(BigDecimal.valueOf(50))
                        .build())
                .pet(pet)
                .customer(customer)
                .employee(employee)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterConsultationSuccessfully() throws Exception {
        ConsultationRequestDTO requestDTO = ConsultationRequestDTO.builder()
                .customerId(customer.getId())
                .petId(pet.getId())
                .petCareRequestDTO(petCareRequest())
                .saleRequestDTO(saleRequest())
                .vaccinationRequestDTO(vaccinationRequest())
                .details(ConsultationDetails.builder()
                        .notes("Notes")
                        .type(ServiceType.LAB_TEST)
                        .consultationStatus(ConsultationStatus.PROCESSING)
                        .priority(ConsultationPriority.HIGH)
                        .paymentType(PaymentType.BANK_TRANSFER)
                        .location(TestUtils.address())
                        .build())
                .build();

        mockMvc.perform(post("/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String JSON = result.getResponse().getContentAsString();
                    ConsultationResponseDTO responseDTO = objectMapper.readValue(JSON, ConsultationResponseDTO.class);

                    assertAll(
                            () -> assertNotNull(responseDTO.saleResponseDTO()),
                            () -> assertNotNull(responseDTO.vaccinationResponseDTO()),
                            () -> assertNotNull(responseDTO.petCareResponseDTO()),
                            () -> assertEquals(customer.getId(), responseDTO.petCareResponseDTO().customerId()),
                            () -> assertEquals(customer.getId(), responseDTO.saleResponseDTO().getCustomerId()),
                            () -> assertEquals(customer.getId(), responseDTO.vaccinationResponseDTO().getCustomerId())
                    );
                });
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindConsultationByIdSuccessfully() throws Exception{
        Consultation consultation = consultation();
        consultationRepository.saveAndFlush(consultation);

        UUID consultationId = consultation.getId();

        mockMvc.perform(get("/consultations/" + consultationId)
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String JSON = result.getResponse().getContentAsString();
                    ConsultationResponseDTO dto = objectMapper.readValue(JSON, ConsultationResponseDTO.class);
                    assertEquals(consultationId, dto.id());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllConsultationByFilterSuccessfully() throws Exception {
        consultationRepository.deleteAll();

        Consultation consultation1 = consultation();
        Consultation consultation2 = consultation();
        Consultation consultation3 = consultation();

        consultationRepository.saveAll(List.of(consultation1, consultation2, consultation3));

        mockMvc.perform(get("/consultations")
                        .param("page", "0")
                        .param("size", "3")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateConsultationByIdSuccessfully() throws Exception{
        Consultation consultation = consultation();
        consultationRepository.save(consultation);

        UUID consultationId = consultation.getId();

        mockMvc.perform(delete("/consultations/" + consultationId)
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isNoContent());

        Optional<Consultation> result = consultationRepository.findById(consultationId);
        assertEquals(StatusEntity.DELETED, result.get().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldToggleStatusConsultationSuccessfully() throws Exception{
        Consultation consultation = consultation();
        consultation.getDetails().setConsultationStatus(ConsultationStatus.OPEN);
        consultationRepository.save(consultation);

        UUID consultationId = consultation.getId();

        mockMvc.perform(patch("/consultations/" + consultationId)
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                })
                .param("status", "COMPLETED"))
                .andExpect(status().isNoContent());

        Optional<Consultation> result = consultationRepository.findById(consultationId);
        assertEquals(ConsultationStatus.COMPLETED, result.get().getDetails().getConsultationStatus());
    }
}
