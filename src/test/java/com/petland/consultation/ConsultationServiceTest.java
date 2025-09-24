package com.petland.consultation;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.consultation.builder.ConsultationFilter;
import com.petland.modules.consultation.calculator.ConsultationCalculator;
import com.petland.modules.consultation.controller.dtos.ConsultationHistoryResponseDTO;
import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.controller.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.model.enums.PaymentType;
import com.petland.modules.consultation.mappers.ConsultationMapperGenerator;
import com.petland.modules.consultation.mappers.ConsultationMapper;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.consultation.repositories.ConsultationRepository;
import com.petland.modules.consultation.service.ConsultationService;
import com.petland.modules.consultation.strategy.factory.ConsultationFactory;
import com.petland.modules.consultation.validator.ConsultationValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.vaccination.service.VaccinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {

    @Mock private ConsultationRepository repository;
    @Mock private PetService petService;
    @Mock private CustomerService customerService;
    @Mock private EmployeeService employeeService;
    @Mock private AccessValidator accessValidator;
    @Mock private PetValidator petValidator;
    @Mock private ConsultationValidator validator;
    @Mock private SaleService saleService;
    @Mock private VaccinationService vaccinationService;
    @Mock private PetCareService petCareService;
    @Mock private ConsultationMapper mapper;
    @Mock private ConsultationFactory factory;
    @Mock private ConsultationCalculator calculator;
    @Mock private ConsultationMapperGenerator generateResponse;
    @InjectMocks private ConsultationService consultationService;

    private UUID customerId;
    private UUID employeeId;
    private UUID petId;

    private Employee employee;
    private Customer customer;
    private Pet pet;
    private Consultation consultation;
    private ConsultationDetails details;
    private BigDecimal valueDefault;
    private UUID consultationId;
    private Pageable pageable;

    @BeforeEach
    void setUp(){
        employeeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        petId = UUID.randomUUID();
        valueDefault = BigDecimal.valueOf(100);
        consultationId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

        employee = Employee.builder().id(UUID.randomUUID()).build();
        customer = Customer.builder().id(UUID.randomUUID()).consultationsHistory(new ArrayList<>()).build();
        pet = Pet.builder().id(UUID.randomUUID()).consultationsHistory(new ArrayList<>()).build();

        details = ConsultationDetails.builder()
                .profitByService(valueDefault)
                .costOperatingByService(valueDefault)
                .totalByService(valueDefault)
                .build();

        consultation = new Consultation();
        consultation.setDetails(details);
        consultation.setId(consultationId);
    }

    private ConsultationRequestDTO consultationRequest(){
        return ConsultationRequestDTO.builder()
                .petId(petId)
                .customerId(customerId)
                .vaccinationRequestDTO(null)
                .saleRequestDTO(null)
                .petCareRequestDTO(null)
                .details(details)
                .build();
    }

    @Test
    void shouldRegisterConsultationSuccessfully(){
        ConsultationRequestDTO request = consultationRequest();

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(customerService.findById(request.customerId())).thenReturn(customer);
        when(employeeService.findById(employeeId)).thenReturn(employee);
        when(petService.findById(request.petId())).thenReturn(pet);

        doNothing().when(validator).validateIfItIsTheSameCustomer(request);
        doNothing().when(petValidator).isPetOwner(pet, customer);

        when(mapper.toEntity(request)).thenReturn(consultation);
        when(factory.execute(consultation, request)).thenReturn(consultation);

        when(calculator.calculateTotalBilling(consultation)).thenReturn(valueDefault);
        when(calculator.calculateTotalProfit(consultation)).thenReturn(valueDefault);
        when(calculator.calculateTotalCostOperating(consultation)).thenReturn(valueDefault);

        when(repository.save(any(Consultation.class))).thenAnswer(inv -> inv.getArgument(0));
        Consultation result = consultationService.registerConsultation(request);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(customer, result.getCustomer()),
                () -> assertEquals(employee, result.getEmployee()),
                () -> assertEquals(pet, result.getPet()),
                () -> assertEquals(valueDefault, result.getDetails().getProfitByService()),
                () -> assertEquals(valueDefault, result.getDetails().getTotalByService()),
                () -> assertEquals(valueDefault, result.getDetails().getCostOperatingByService())
        );

        verify(employeeService).findById(employeeId);
        verify(customerService).findById(customerId);
        verify(petService).findById(petId);
        verify(factory).execute(consultation, request);
        verify(calculator).calculateTotalBilling(consultation);
        verify(calculator).calculateTotalProfit(consultation);
        verify(calculator).calculateTotalCostOperating(consultation);
        verify(validator).validateIfItIsTheSameCustomer(request);
        verify(petValidator).isPetOwner(pet, customer);
        verify(repository).save(any(Consultation.class));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        ConsultationRequestDTO dto = consultationRequest();

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);

        doThrow(new NotFoundException("Employer not found"))
                .when(employeeService).findById(employeeId);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> consultationService.registerConsultation(dto)
        );
        assertEquals("Employer not found", ex.getMessage());

        verify(accessValidator).getLoggedInUser();
        verify(employeeService).findById(employeeId);
        verify(repository, never()).save(consultation);
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound(){
        ConsultationRequestDTO dto = consultationRequest();

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(employeeService.findById(employeeId)).thenReturn(employee);

        doThrow(new NotFoundException("Customer ID not found"))
                .when(customerService).findById(customerId);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> consultationService.registerConsultation(dto)
        );
        assertEquals("Customer ID not found", ex.getMessage());

        verify(accessValidator).getLoggedInUser();
        verify(employeeService).findById(employeeId);
        verify(repository, never()).save(consultation);
    }

    @Test
    void shouldThrowExceptionWhenPetIsNotFound(){
        ConsultationRequestDTO dto = consultationRequest();

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(employeeService.findById(employeeId)).thenReturn(employee);

        doThrow(new NotFoundException("Pet ID not found"))
                .when(petService).findById(petId);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> consultationService.registerConsultation(dto)
        );
        assertEquals("Pet ID not found", ex.getMessage());

        verify(petService).findById(petId);
        verify(repository, never()).save(consultation);
    }

    @Test
    @DisplayName("Must throw exception when customer ID differs from sales, vaccinations or services ")
    void shouldThrowExceptionWhenCustomerIdDiffersFromAssociatedRecords(){
        ConsultationRequestDTO request = consultationRequest();
        String messageException = "The sale must be registered to the same customer as the one associated with the consultation.";

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(employeeService.findById(employeeId)).thenReturn(employee);
        when(customerService.findById(customerId)).thenReturn(customer);
        when(petService.findById(petId)).thenReturn(pet);

        doThrow(new UnauthorizedException(messageException))
                .when(validator).validateIfItIsTheSameCustomer(request);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> consultationService.registerConsultation(request)
        );

        assertEquals(messageException, ex.getMessage());

        verify(employeeService).findById(employeeId);
        verify(customerService).findById(customerId);
        verify(petService).findById(petId);
        verify(repository, never()).save(consultation);
    }

    @Test
    void shouldThrowExceptionIfThePetDoesNotBelongToTheCustomer(){
        ConsultationRequestDTO request = consultationRequest();
        String messageException = "This pet does not belong to this customer";

        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(employeeService.findById(employeeId)).thenReturn(employee);
        when(customerService.findById(customerId)).thenReturn(customer);
        when(petService.findById(petId)).thenReturn(pet);

        doNothing().when(validator).validateIfItIsTheSameCustomer(request);

        doThrow(new UnauthorizedException(messageException))
                .when(petValidator).isPetOwner(pet, customer);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> consultationService.registerConsultation(request)
        );

        assertEquals(messageException, ex.getMessage());

        verify(validator).validateIfItIsTheSameCustomer(request);
        verify(employeeService).findById(employeeId);
        verify(customerService).findById(customerId);
        verify(petService).findById(petId);
        verify(repository, never()).save(consultation);
    }

    @Test
    void shouldReturnConsultationWhenFoundAndNotDeleted() {
        consultation.setStatus(StatusEntity.ACTIVE);
        when(repository.findById(consultationId)).thenReturn(Optional.of(consultation));

        Consultation result = consultationService.findById(consultationId);

        assertNotNull(result);
        assertEquals(consultationId, result.getId());
        verify(repository).findById(consultationId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenConsultationIsDeleted() {
        consultation.setStatus(StatusEntity.DELETED);

        when(repository.findById(consultationId)).thenReturn(Optional.of(consultation));
        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> consultationService.findById(consultationId)
        );
        assertEquals("Consultation ID not found", ex.getMessage());
        verify(repository).findById(consultationId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenConsultationNotFound() {
        when(repository.findById(consultationId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> consultationService.findById(consultationId)
        );
        assertEquals("Consultation ID not found", ex.getMessage());
        verify(repository).findById(consultationId);
    }

    @Test
    void shouldReturnConsultationHistoryPageWhenClientHasValidConsultations() {
        Consultation consultation2 = new Consultation();
        consultation2.setStatus(StatusEntity.DELETED);

        ConsultationHistoryResponseDTO dto1 = ConsultationHistoryResponseDTO.builder().build();

        when(repository.findAllByCustomerId(customerId, pageable))
                .thenReturn(new PageImpl<>(List.of(consultation, consultation2), pageable, 2));

        when(generateResponse.mapToCustomerHistory(consultation)).thenReturn(dto1);

        Page<ConsultationHistoryResponseDTO> result = consultationService.listAllConsultationsByClientId(customerId, pageable);

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
    }

    @Test
    void shouldReturnConsultationPageWhenFilterMatchesValidConsultations() {
        Consultation consultation2 = new Consultation();
        Consultation consultation3 = new Consultation();

        ConsultationFilter filter = ConsultationFilter.builder()
                .status(StatusEntity.ACTIVE)
                .paymentType(PaymentType.BANK_TRANSFER).build();

        consultation.setStatus(StatusEntity.ACTIVE);
        consultation.getDetails().setPaymentType(PaymentType.BANK_TRANSFER);

        consultation2.setStatus(StatusEntity.DELETED);
        consultation3.setStatus(StatusEntity.ACTIVE);

        ConsultationResponseDTO dto1 = ConsultationResponseDTO.builder().build();
        ConsultationResponseDTO dto3 = ConsultationResponseDTO.builder().build();

        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(consultation, consultation2, consultation3), pageable, 3));

        when(generateResponse.generateResponse(consultation)).thenReturn(dto1);
        when(generateResponse.generateResponse(consultation3)).thenReturn(dto3);

        Page<ConsultationResponseDTO> result = consultationService.listAllConsultationsByFilter(filter, pageable);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
        assertTrue(result.getContent().contains(dto3));

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}
