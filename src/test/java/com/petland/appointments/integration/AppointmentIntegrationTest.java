package com.petland.appointments.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.dtos.AppointmentUpdateDTO;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
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
class AppointmentIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    private Customer customer;
    private Pet pet;
    private Employee employee;

    @BeforeEach
    void cleanBeforeTest() {
        appointmentRepository.deleteAll();
        employeeRepository.deleteAll();
        customerRepository.deleteAll();
        petRepository.deleteAll();

        employee = TestUtils.employee();
        customer = TestUtils.customer();
        pet = TestUtils.pet();
        customer.getMyPets().add(pet);
        pet.setOwner(customer);
        customerRepository.save(customer);
        employeeRepository.save(employee);
    }

    @AfterEach
    void cleanAfterTest(){
        appointmentRepository.deleteAll();
        employeeRepository.deleteAll();
        customerRepository.deleteAll();
        petRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Schedules an appointment when requested by an customer")
    void shouldScheduleAppointmentAndReturnPdfByCustomer() throws Exception {
        AppointmentRequestDTO dto = AppointmentRequestDTO.builder()
                .petId(pet.getId())
                .customerId(customer.getId())
                .appointmentType(ServiceType.FULL_GROOMING)
                .appointmentHour(LocalTime.now())
                .appointmentDate(LocalDate.now().plusDays(5))
                .build();

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Schedules an appointment when requested by an employee")
    void shouldScheduleAppointmentAndReturnPdfByEmployee() throws Exception {
        AppointmentRequestDTO dto = AppointmentRequestDTO.builder()
                .petId(pet.getId())
                .customerId(customer.getId())
                .appointmentType(ServiceType.FULL_GROOMING)
                .appointmentHour(LocalTime.now())
                .appointmentDate(LocalDate.now().plusDays(5))
                .build();

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Throws exception when a customer tries to schedule an appointment for another customer's pet")
    void shouldThrowExceptionWhenUserIsNotAuthorized() throws Exception{
        Customer customerOther = TestUtils.customer();
        customerOther.setEmail("customer@gmail.com");
        customerRepository.saveAndFlush(customerOther);

        AppointmentRequestDTO dto = AppointmentRequestDTO.builder()
                .petId(pet.getId())
                .customerId(customerOther.getId())
                .appointmentType(ServiceType.FULL_GROOMING)
                .appointmentHour(LocalTime.now())
                .appointmentDate(LocalDate.now().plusDays(5))
                .build();

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> {
                    assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
                    assertInstanceOf(UnauthorizedException.class, result.getResolvedException());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAppointmentByIdSuccessfully() throws Exception{
        Appointment appointment = TestUtils.appointment();
        appointment.setCustomer(customer);
        appointment.setPet(pet);
        appointmentRepository.saveAndFlush(appointment);

        UUID appointmentId = appointment.getId();

        mockMvc.perform(get("/appointments/" + appointmentId.toString())
                .with(request -> {
                    request.setAttribute("id", employee.getId());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String JSON = result.getResponse().getContentAsString();
                    AppointmentResponseDTO appointmentResult = objectMapper.readValue(JSON, AppointmentResponseDTO.class);
                    assertEquals(appointmentId, appointmentResult.id());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowExceptionWhenAppointmentIdIsNotFound() throws Exception{
        UUID appointmentId = UUID.randomUUID();

        mockMvc.perform(get("/appointments/" + appointmentId)
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
                    assertInstanceOf(NotFoundException.class, result.getResolvedException());
                });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRescheduleAppointmentSuccessfully() throws Exception{
        Appointment appointment = TestUtils.appointment();
        appointment.setCustomer(customer);
        appointment.setPet(pet);

        appointmentRepository.saveAndFlush(appointment);

        AppointmentUpdateDTO updateDTO = AppointmentUpdateDTO.builder()
                .appointmentDate(LocalDate.now().plusDays(2))
                .appointmentHour(LocalTime.now())
                .appointmentType(ServiceType.FULL_GROOMING)
                .build();

        mockMvc.perform(put("/appointments/" + appointment.getId())
                .content(objectMapper.writeValueAsString(updateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                        .with(request -> {
                            request.setAttribute("id", customer.getId());
                            return request;
                        }))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCancelAppointmentSuccessfully() throws Exception {
        Appointment appointment = TestUtils.appointment();
        appointment.setCustomer(customer);
        appointment.setPet(pet);
        appointmentRepository.saveAndFlush(appointment);

        mockMvc.perform(MockMvcRequestBuilders.patch("/appointments/" + appointment.getId())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Optional<Appointment> result = appointmentRepository.findById(appointment.getId());

        assertEquals(appointment.getId(), result.get().getId());
        assertEquals(AppointmentStatus.CANCELED, result.get().getAppointmentStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldToggleStatusSuccessfully() throws Exception{
        Appointment appointment = TestUtils.appointment();
        appointment.setCustomer(customer);
        appointment.setPet(pet);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.saveAndFlush(appointment);

        AppointmentStatus status = AppointmentStatus.COMPLETED;

        mockMvc.perform(patch("/appointments/" + appointment.getId() + "/toggle-status")
                        .param("status", status.toString())
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isNoContent());

        Optional<Appointment> result = appointmentRepository.findById(appointment.getId());
        assertEquals(status, result.get().getAppointmentStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAppointmentsListSuccessfully() throws Exception{
        Appointment appointment1 = TestUtils.appointment(); appointment1.setCustomer(customer); appointment1.setPet(pet);
        Appointment appointment2 = TestUtils.appointment(); appointment2.setCustomer(customer); appointment2.setPet(pet);
        Appointment appointment3 = TestUtils.appointment(); appointment3.setCustomer(customer); appointment3.setPet(pet);

        appointmentRepository.saveAll(List.of(appointment1, appointment2, appointment3));

        mockMvc.perform(get("/appointments")
                        .param("page", "0")
                        .param("size", "3")
                        .with(request -> {
                            request.setAttribute("id", employee.getId());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }
}
