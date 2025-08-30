package com.petland.petCare;

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
import com.petland.modules.petCare.builder.PetCareFilter;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.petCare.service.PetCareDetailsService;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetCareServiceTest {

    @Mock private PetService petService;
    @Mock private CustomerService customerService;
    @Mock private EmployeeService employeeService;
    @Mock private PetValidator petValidator;
    @Mock private PetCareCalculator calculator;
    @Mock private PetCareRepository repository;
    @Mock private PetCareDetailsService petCareDetailsService;
    @Mock private GeneratePetCareResponse generateResponse;
    @InjectMocks PetCareService service;

    private Customer customer;
    private Employee employee;
    private Pet pet;
    private List<PetCareDetails> petCareDetails;

    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private BigDecimal totalCostOperating;
    private PetCareRequestDTO petCareRequest;

    @BeforeEach
    void setUp(){
        petCareRequest = PetCareRequestDTO.builder().petId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .serviceDetailsList(List.of(PetCareDetailsRequestDTO.builder().build()))
                .location(new Address())
                .employeeId(UUID.randomUUID())
                .build();

        employee = new Employee();
        customer = new Customer();
        pet = new Pet();
        customer.getMyPets().add(pet);
        pet.setOwner(customer);

        petCareDetails = new ArrayList<>();
        petCareDetails.add(new PetCareDetails());

        totalRevenue = BigDecimal.valueOf(200);
        totalProfit = BigDecimal.valueOf(150);
        totalCostOperating = BigDecimal.valueOf(50);
    }

    @Test
    void shouldRegisterPetCareServiceSuccessfully(){
        when(customerService.findById(petCareRequest.customerId())).thenReturn(customer);
        when(petService.findById(petCareRequest.petId())).thenReturn(pet);
        when(employeeService.findById(petCareRequest.employeeId())).thenReturn(employee);

        when(petCareDetailsService.createService(petCareRequest.serviceDetailsList())).thenReturn(petCareDetails);
        when(calculator.calculateTotalRevenueByServiceList(petCareDetails)).thenReturn(totalRevenue);
        when(calculator.calculateTotalProfitByServiceList(petCareDetails)).thenReturn(totalProfit);
        when(calculator.calculateTotalCostOperatingByServiceList(petCareDetails)).thenReturn(totalCostOperating);

        PetCare petCareExpected = PetCare.builder()
                .id(UUID.randomUUID())
                .serviceDate(LocalDateTime.now())
                .pet(pet)
                .customer(customer)
                .employee(employee)
                .totalCostOperating(totalCostOperating)
                .totalProfit(totalProfit)
                .totalRevenue(totalRevenue)
                .location(petCareRequest.location())
                .petCareDetails(petCareDetails)
                .build();

        when(repository.save(any(PetCare.class))).thenReturn(petCareExpected);

        PetCare result = service.register(petCareRequest);

        assertAll(
                () -> assertEquals(petCareExpected.getPet(), result.getPet()),
                () -> assertEquals(petCareExpected.getTotalRevenue(), result.getTotalRevenue()),
                () -> assertEquals(petCareExpected.getTotalProfit(), result.getTotalProfit()),
                () -> assertEquals(petCareExpected.getTotalCostOperating(), result.getTotalCostOperating()),
                () -> assertEquals(petCareExpected.getCustomer(), result.getCustomer()),
                () -> assertNotNull(result.getPetCareDetails())
        );

        verify(customerService).findById(petCareRequest.customerId());
        verify(petService).findById(petCareRequest.petId());
        verify(employeeService).findById(petCareRequest.employeeId());
        verify(petValidator).isPetOwner(pet, customer);
        verify(petCareDetailsService).createService(petCareRequest.serviceDetailsList());
        verify(calculator).calculateTotalRevenueByServiceList(petCareDetails);
        verify(calculator).calculateTotalProfitByServiceList(petCareDetails);
        verify(calculator).calculateTotalCostOperatingByServiceList(petCareDetails);
        verify(repository).save(any(PetCare.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound(){
        doThrow(new NotFoundException("Customer ID not found"))
                .when(customerService).findById(petCareRequest.customerId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(petCareRequest)
        );
        assertEquals("Customer ID not found", ex.getMessage());

        verify(customerService).findById(petCareRequest.customerId());
        verify(repository, never()).save(any(PetCare.class));
    }

    @Test
    void shouldThrowExceptionWhenPetIsNotFound(){
        doThrow(new NotFoundException("Pet ID not found"))
                .when(petService).findById(petCareRequest.petId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(petCareRequest)
        );
        assertEquals("Pet ID not found", ex.getMessage());

        verify(petService).findById(petCareRequest.petId());
        verify(repository, never()).save(any(PetCare.class));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        doThrow(new NotFoundException("Employee ID not found"))
                .when(employeeService).findById(petCareRequest.employeeId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(petCareRequest)
        );
        assertEquals("Employee ID not found", ex.getMessage());

        verify(employeeService).findById(petCareRequest.employeeId());
        verify(repository, never()).save(any(PetCare.class));
    }
    @Test
    void shouldThrowExceptionWhenPetDoesNotBelongToCustomer(){
        when(customerService.findById(petCareRequest.customerId())).thenReturn(customer);
        when(petService.findById(petCareRequest.petId())).thenReturn(pet);
        when(employeeService.findById(petCareRequest.employeeId())).thenReturn(employee);

        doThrow(new UnauthorizedException("This pet does not belong to this customer"))
                .when(petValidator).isPetOwner(pet, customer);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> service.register(petCareRequest)
        );

        assertEquals("This pet does not belong to this customer", ex.getMessage());

        verify(customerService).findById(petCareRequest.customerId());
        verify(petService).findById(petCareRequest.petId());
        verify(employeeService).findById(petCareRequest.employeeId());
        verify(petValidator).isPetOwner(pet, customer);
        verify(repository, never()).save(any(PetCare.class));
    }

    @Test
    void shouldReturnHistoryByCustomerId_whenExists() {
        UUID customerId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        PetCare petCare = new PetCare();
        PetCareHistoryResponseDTO dto = PetCareHistoryResponseDTO.builder().build();

        Page<PetCare> page = new PageImpl<>(List.of(petCare));

        when(repository.findByCustomerId(customerId, pageable)).thenReturn(page);
        when(generateResponse.mapToCustomerServiceHistory(petCare)).thenReturn(dto);

        Page<PetCareHistoryResponseDTO> result = service.findAllByCustomerId(customerId, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));

        verify(repository).findByCustomerId(customerId, pageable);
        verify(generateResponse).mapToCustomerServiceHistory(petCare);
    }

    @Test
    void shouldThrowNotFoundException_whenCustomerHistoryEmpty() {
        UUID customerId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByCustomerId(customerId, pageable)).thenReturn(Page.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findAllByCustomerId(customerId, pageable)
        );
        assertEquals("Customer service history list not found", ex.getMessage());
        verify(repository).findByCustomerId(customerId, pageable);
    }


    @Test
    void shouldReturnPetCare_whenExistsAndNotDeleted() {
        UUID petCareId = UUID.randomUUID();
        PetCare petCare = new PetCare();
        petCare.setStatus(StatusEntity.ACTIVE);

        when(repository.findById(petCareId)).thenReturn(Optional.of(petCare));

        PetCare result = service.findById(petCareId);

        assertEquals(petCare, result);
        verify(repository).findById(petCareId);
    }

    @Test
    void shouldThrowNotFoundException_whenNotFoundOrDeleted() {
        UUID petCareId = UUID.randomUUID();

        when(repository.findById(petCareId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findById(petCareId));

        PetCare deleted = new PetCare();
        deleted.setStatus(StatusEntity.DELETED);

        when(repository.findById(petCareId)).thenReturn(Optional.of(deleted));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findById(petCareId)
        );
        assertEquals("PetCare ID not found", ex.getMessage());

        verify(repository, times(2)).findById(petCareId);


    }


    @Test
    void shouldDeactivatePetCareAndDetails() {
        UUID id = UUID.randomUUID();
        PetCareDetails detail = new PetCareDetails();
        detail.setStatus(StatusEntity.ACTIVE);

        PetCare petCare = new PetCare();
        petCare.setId(id);
        petCare.setStatus(StatusEntity.ACTIVE);
        petCare.setPetCareDetails(List.of(detail));

        when(repository.findById(id)).thenReturn(Optional.of(petCare));

        service.deactivateById(id);

        assertEquals(StatusEntity.DELETED, petCare.getStatus());
        assertEquals(StatusEntity.DELETED, detail.getStatus());
        verify(repository).save(petCare);
    }

    @Test
    void shouldReturnPetCareResponseDTOPage() {
        PetCare petCare = new PetCare();
        PetCareResponseDTO dto = PetCareResponseDTO.builder().build();

        Page<PetCare> petCarePage = new PageImpl<>(List.of(petCare));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(petCarePage);
        when(generateResponse.generate(petCare)).thenReturn(dto);

        Page<PetCareResponseDTO> result = service.findAllByFilter(new PetCareFilter(), pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void shouldReturnOnlyNonDeletedPetCaresInPeriod() {
        LocalDate min = LocalDate.now().minusDays(10);
        LocalDate max = LocalDate.now();

        PetCare active = new PetCare();
        active.setStatus(StatusEntity.ACTIVE);

        PetCare deleted = new PetCare();
        deleted.setStatus(StatusEntity.DELETED);

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(active, deleted));

        List<PetCare> result = service.findAllByPeriod(min, max);

        assertEquals(1, result.size());
        assertEquals(StatusEntity.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void shouldReturnOnlyNonDeletedPetCaresByType() {
        PetCareType type = PetCareType.CHECKUP;

        PetCare active = new PetCare();
        active.setStatus(StatusEntity.ACTIVE);

        PetCare deleted = new PetCare();
        deleted.setStatus(StatusEntity.DELETED);

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(active, deleted));

        List<PetCare> result = service.findAllByPetCareType(type);

        assertEquals(1, result.size());
        assertEquals(StatusEntity.ACTIVE, result.get(0).getStatus());
    }

}
