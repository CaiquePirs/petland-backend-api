package com.petland.vaccination;

import com.petland.common.entity.Address;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import com.petland.modules.vaccination.builder.VaccinationFilter;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.AppliedVaccineRepository;
import com.petland.modules.vaccination.repository.VaccinationRepository;
import com.petland.modules.vaccination.service.AppliedVaccineService;
import com.petland.modules.vaccination.service.VaccinationService;
import com.petland.modules.vaccination.service.VaccineService;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.validator.VaccinationUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VaccinationServiceTest {

    @Mock private EmployeeService employeeService;
    @Mock private CustomerService customerService;
    @Mock private PetService petService;
    @Mock private AppliedVaccineService appliedVaccineService;
    @Mock private AppliedVaccineRepository appliedVaccineRepository;
    @Mock private VaccinationCalculator calculator;
    @Mock private VaccinationRepository vaccinationRepository;
    @Mock private VaccinationUpdateValidator validator;
    @Mock private GenerateVaccinationResponse generateResponse;
    @Mock private VaccineService vaccineService;
    @Mock private PetValidator petValidator;
    @InjectMocks private VaccinationService service;

    private Employee employee;
    private Customer customer;
    private Pet pet;
    private VaccinationRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        requestDTO = new VaccinationRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(AppliedVaccineRequestDTO.builder().build()),
                LocalDate.now(), LocalDate.now(), new Address(), "Notes"
        );

        employee = Employee.builder().id(requestDTO.veterinarianId()).build();
        customer = Customer.builder().id(requestDTO.customerId()).myPets(new ArrayList<>()).build();
        pet = Pet.builder().id(requestDTO.petId()).owner(customer).vaccinationsHistory(new ArrayList<>()).build();
    }

    private Vaccination vaccination(){
        return Vaccination.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .veterinarian(employee)
                .pet(pet)
                .totalByVaccination(BigDecimal.valueOf(100.00))
                .profitByVaccination(BigDecimal.valueOf(50.00))
                .clinicalNotes(requestDTO.clinicalNotes())
                .location(requestDTO.location())
                .nextDoseDate(requestDTO.nextDoseDate())
                .vaccinationDate(requestDTO.vaccinationDate())
                .build();
    }

    @Test
    void shouldRegisterVaccinationSuccessfully(){
        customer.getMyPets().add(pet);

        when(petService.findById(requestDTO.petId())).thenReturn(pet);
        when(customerService.findById(requestDTO.customerId())).thenReturn(customer);
        when(employeeService.findById(requestDTO.veterinarianId())).thenReturn(employee);
        doNothing().when(petValidator).isPetOwner(pet, customer);

        List<AppliedVaccine> appliedVaccines = new ArrayList<>(); appliedVaccines.add(new AppliedVaccine());
        when(appliedVaccineService.create(requestDTO.listAppliedVaccineRequestDTO())).thenReturn(appliedVaccines);
        when(calculator.calculateTotalVaccination(appliedVaccines)).thenReturn(BigDecimal.valueOf(100.00));

        Vaccination vaccinationExpected = vaccination();
        vaccinationExpected.setAppliedVaccines(appliedVaccines);

        when(vaccinationRepository.save(any(Vaccination.class))).thenReturn(vaccinationExpected);

        Vaccination result = service.register(requestDTO);

        assertAll(
                () -> assertNotNull(result.getAppliedVaccines()),
                () -> assertEquals(customer.getId(), result.getCustomer().getId()),
                () -> assertEquals(employee.getId(), result.getVeterinarian().getId()),
                () -> assertEquals(pet.getId(), result.getPet().getId()),
                () -> assertTrue(result.getCustomer().getMyPets().contains(pet)),
                () -> assertEquals(BigDecimal.valueOf(100.00), result.getTotalByVaccination()),
                () -> assertEquals(BigDecimal.valueOf(50.00), result.getProfitByVaccination())
        );

        verify(employeeService).findById(requestDTO.veterinarianId());
        verify(customerService).findById(requestDTO.customerId());
        verify(petService).findById(requestDTO.petId());
        verify(calculator).calculateTotalVaccination(appliedVaccines);
        verify(calculator).calculateProfitByVaccineApplied(appliedVaccines);
        verify(vaccinationRepository).save(any(Vaccination.class));
    }

    @Test
    void shouldThrowExceptionWhenPetIsNotFound(){
        doThrow(new NotFoundException("Pet ID not found")).when(petService).findById(requestDTO.petId());

       NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(requestDTO)
       );
       assertEquals("Pet ID not found", ex.getMessage());

       verify(petService).findById(requestDTO.petId());
       verify(vaccinationRepository, never()).save(any(Vaccination.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound(){
        doThrow(new NotFoundException("Customer ID not found")).when(customerService).findById(requestDTO.customerId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(requestDTO)
        );
        assertEquals("Customer ID not found", ex.getMessage());

        verify(customerService).findById(requestDTO.customerId());
        verify(vaccinationRepository, never()).save(any(Vaccination.class));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        doThrow(new NotFoundException("Employee ID not found")).when(employeeService).findById(requestDTO.veterinarianId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(requestDTO)
        );
        assertEquals("Employee ID not found", ex.getMessage());

        verify(employeeService).findById(requestDTO.veterinarianId());
        verify(vaccinationRepository, never()).save(any(Vaccination.class));
    }

    @Test
    void shouldThrowExceptionWhenPetDoesNotBelongToCustomer() {
        when(customerService.findById(requestDTO.customerId())).thenReturn(customer);
        when(petService.findById(requestDTO.petId())).thenReturn(pet);
        when(employeeService.findById(requestDTO.veterinarianId())).thenReturn(employee);

        doThrow(new UnauthorizedException("This pet does not belong to this customer"))
                .when(petValidator).isPetOwner(pet, customer);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> service.register(requestDTO)
        );

        assertEquals("This pet does not belong to this customer", ex.getMessage());

        verify(customerService).findById(requestDTO.customerId());
        verify(employeeService).findById(requestDTO.veterinarianId());
        verify(petService).findById(requestDTO.petId());
        verify(vaccinationRepository, never()).save(any(Vaccination.class));
    }

    @Test
    void shouldReturnVaccinationWhenFoundById(){
        UUID vaccinationId = UUID.randomUUID();
        Vaccination vaccination = vaccination();
        vaccination.setId(vaccinationId);
        vaccination.setStatus(StatusEntity.ACTIVE);

        when(vaccinationRepository.findById(vaccinationId)).thenReturn(Optional.of(vaccination));

        Vaccination result = service.findById(vaccinationId);

        assertEquals(vaccinationId, result.getId());
        assertEquals(StatusEntity.ACTIVE, result.getStatus());
        verify(vaccinationRepository).findById(vaccinationId);
    }

    @Test
    void shouldThrowExceptionWhenVaccinationIsNotFound(){
        UUID vaccinationId = UUID.randomUUID();

        when(vaccinationRepository.findById(vaccinationId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findById(vaccinationId)
        );

        assertEquals("Vaccination not found", ex.getMessage());
        verify(vaccinationRepository).findById(vaccinationId);
    }

    @Test
    void shouldThrowExceptionWhenVaccinationIsStatusDeleted(){
        UUID vaccinationId = UUID.randomUUID();
        Vaccination vaccination = vaccination();
        vaccination.setId(vaccinationId);
        vaccination.setStatus(StatusEntity.DELETED);

        when(vaccinationRepository.findById(vaccinationId)).thenReturn(Optional.of(vaccination));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findById(vaccinationId)
        );

        assertEquals("Vaccination not found", ex.getMessage());
        verify(vaccinationRepository).findById(vaccinationId);
    }

    @Test
    void shouldDeactivateVaccinationWithoutAppliedVaccines() {
        UUID id = UUID.randomUUID();
        Vaccination vaccination = new Vaccination();
        vaccination.setStatus(StatusEntity.ACTIVE);
        vaccination.setAppliedVaccines(List.of());

        when(vaccinationRepository.findById(id)).thenReturn(Optional.of(vaccination));

        service.deactivateById(id);

        assertThat(vaccination.getStatus()).isEqualTo(StatusEntity.DELETED);
        verify(appliedVaccineRepository, never()).saveAll(any());
        verify(vaccinationRepository).save(vaccination);
    }

    @Test
    void shouldUpdateVaccinationUsingValidator() {
        UUID id = UUID.randomUUID();
        Vaccination vaccination = new Vaccination();
        VaccinationUpdateDTO dto = VaccinationUpdateDTO.builder().build();

        when(vaccinationRepository.findById(id)).thenReturn(Optional.of(vaccination));
        when(validator.validate(vaccination, dto)).thenReturn(vaccination);

        Vaccination result = service.updateById(dto, id);

        assertThat(result).isEqualTo(vaccination);
        verify(validator).validate(vaccination, dto);
    }

    @Test
    void shouldListAllVaccinationsByFilter() {
        VaccinationFilter filter = new VaccinationFilter();
        Pageable pageable = mock(Pageable.class);

        Vaccination vaccination = new Vaccination();
        Page<Vaccination> page = new PageImpl<>(List.of(vaccination));
        VaccinationResponseDTO responseDTO = new VaccinationResponseDTO();

        when(vaccinationRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(generateResponse.generate(vaccination)).thenReturn(responseDTO);

        Page<VaccinationResponseDTO> result = service.listAllVaccinationsByFilter(filter, pageable);

        assertThat(result.getContent()).containsExactly(responseDTO);
        verify(vaccinationRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldFindAllVaccinationsByPeriodExcludingDeleted() {
        Vaccination v1 = new Vaccination();
        v1.setStatus(StatusEntity.ACTIVE);
        Vaccination v2 = new Vaccination();
        v2.setStatus(StatusEntity.DELETED);

        when(vaccinationRepository.findAll(any(Specification.class))).thenReturn(List.of(v1, v2));

        List<Vaccination> result = service.findAllVaccinationsByPeriod(LocalDate.now(), LocalDate.now());

        assertThat(result).containsExactly(v1);
        verify(vaccinationRepository).findAll(any(Specification.class));
    }

    @Test
    void shouldFindAllVaccinationsByVaccineExcludingDeleted() {
        UUID vaccineId = UUID.randomUUID();
        Vaccine vaccine = new Vaccine();

        Vaccination v1 = new Vaccination();
        v1.setStatus(StatusEntity.ACTIVE);
        Vaccination v2 = new Vaccination();
        v2.setStatus(StatusEntity.DELETED);

        when(vaccineService.findById(vaccineId)).thenReturn(vaccine);
        when(vaccinationRepository.findAll(any(Specification.class))).thenReturn(List.of(v1, v2));

        List<Vaccination> result = service.findAllVaccinationsByVaccine(vaccineId);

        assertThat(result).containsExactly(v1);
        verify(vaccineService).findById(vaccineId);
        verify(vaccinationRepository).findAll(any(Specification.class));
    }


}
