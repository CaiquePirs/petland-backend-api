package com.petland.customer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.repositories.ConsultationRepository;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerProfileIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private SaleRepository saleRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private PetCareRepository petCareRepository;
    @Autowired private ConsultationRepository consultationRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    @BeforeEach
    void cleanBeforeTest(){
        saleRepository.deleteAll();
        consultationRepository.deleteAll();
        appointmentRepository.deleteAll();
        productRepository.deleteAll();
        petCareRepository.deleteAll();

        productRepository.deleteAll();
        employeeRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @AfterEach
    void cleanAfterTest(){
        saleRepository.deleteAll();
        consultationRepository.deleteAll();
        appointmentRepository.deleteAll();
        productRepository.deleteAll();
        petCareRepository.deleteAll();

        productRepository.deleteAll();
        employeeRepository.deleteAll();
        petRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private Sale sale() {
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Product product = TestUtils.product();
        product.setEmployee(employee);
        productRepository.save(product);

        Sale sale = Sale.builder()
                .profitSale(BigDecimal.valueOf(40.00))
                .totalSales(BigDecimal.valueOf(80.00))
                .paymentType(PaymentType.BANK_TRANSFER)
                .employee(employee)
                .build();

        ItemsSale item = ItemsSale.builder()
                .productPrice(product.getCostPrice())
                .productQuantity(2)
                .profit(BigDecimal.valueOf(20.00))
                .itemsSaleTotal(BigDecimal.valueOf(40.00))
                .product(product)
                .sale(sale)
                .build();

        sale.setItemsSale(List.of(item));
        return sale;
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetCustomerLogged() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        UUID customerLoggedId = customer.getId();

        mockMvc.perform(get("/customers/me")
                .with(request -> {
                    request.setAttribute("id", customerLoggedId);
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    CustomerResponseDTO response = objectMapper.readValue(json, CustomerResponseDTO.class);
                    assertAll(
                            () -> assertEquals(customer.getId(), response.id()),
                            () -> assertEquals(customerLoggedId, response.id()),
                            () -> assertEquals(customer.getName(), response.name())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetAllPetsByCustomerLogged() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet1 = TestUtils.pet(); pet1.setOwner(customer);
        Pet pet2 = TestUtils.pet(); pet2.setOwner(customer);
        customer.getMyPets().add(pet1); customer.getMyPets().add(pet2);

        customerRepository.save(customer);

        UUID customerLoggedId = customer.getId();

        mockMvc.perform(get("/customers/me/pets")
                .with(request -> {
                    request.setAttribute("id", customerLoggedId);
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<PetResponseDTO> petsResult = objectMapper.readValue(json, List.class);
                    assertEquals(2, petsResult.size());
                });
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetAllSalesByCustomerLogged() throws Exception{
        Customer customer = TestUtils.customer();
        customerRepository.save(customer);

        Sale sale1 = sale(); sale1.setCustomer(customer);
        Sale sale2 = sale(); sale2.setCustomer(customer);
        Sale sale3 = sale(); sale3.setCustomer(customer);

        saleRepository.saveAll(List.of(sale1, sale2, sale3));

        mockMvc.perform(get("/customers/me/sales/history")
                .param("page", "0")
                .param("size", "10")
                .with(request -> {
                    request.setAttribute("id", customer.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetAllPetCareServicesByCustomerLogged() throws Exception {
       Employee employee = TestUtils.employee();
       employeeRepository.save(employee);

        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        PetCare petCare1 = TestUtils.petCare(); petCare1.setCustomer(customer); petCare1.setPet(pet); petCare1.setEmployee(employee);
        PetCare petCare2 = TestUtils.petCare(); petCare2.setCustomer(customer); petCare2.setPet(pet); petCare2.setEmployee(employee);
        PetCare petCare3 = TestUtils.petCare(); petCare3.setCustomer(customer); petCare3.setPet(pet); petCare3.setEmployee(employee);

        petCareRepository.saveAll(List.of(petCare1, petCare2, petCare3));

        mockMvc.perform(get("/customers/me/pet-care/history")
                        .param("page", "0")
                        .param("size", "10")
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetAllConsultationsByCustomerLogged() throws Exception{
        Employee employee = TestUtils.employee();
        employeeRepository.save(employee);

        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        Consultation consultation1 = TestUtils.consultation();
        Consultation consultation2 = TestUtils.consultation();
        Consultation consultation3 = TestUtils.consultation();

        consultation1.setCustomer(customer); consultation1.setPet(pet); consultation1.setEmployee(employee);
        consultation2.setCustomer(customer); consultation2.setPet(pet); consultation2.setEmployee(employee);
        consultation3.setCustomer(customer); consultation3.setPet(pet); consultation3.setEmployee(employee);

        consultationRepository.saveAll(List.of(consultation1, consultation2, consultation3));

        mockMvc.perform(get("/customers/me/consultations/history")
                        .param("page", "0")
                        .param("size", "10")
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldGetAllAppointmentsByCustomerLogged() throws Exception{
        Customer customer = TestUtils.customer();
        Pet pet = TestUtils.pet();
        customer.getMyPets().add(pet); pet.setOwner(customer);
        customerRepository.save(customer);

        Appointment appointment1 = TestUtils.appointment(); appointment1.setCustomer(customer);
        Appointment appointment2 = TestUtils.appointment(); appointment2.setCustomer(customer);
        Appointment appointment3 = TestUtils.appointment(); appointment3.setCustomer(customer);

        appointmentRepository.saveAll(List.of(appointment1, appointment2, appointment3));

        mockMvc.perform(get("/customers/me/appointments/history")
                        .param("page", "0")
                        .param("size", "10")
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }
}
