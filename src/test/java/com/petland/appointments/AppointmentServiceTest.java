package com.petland.appointments;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.exception.InvalidAppointmentException;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.mapper.AppointmentMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.service.AppointmentService;
import com.petland.modules.appointment.validator.AppointmentValidator;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private PetService petService;
    @Mock private CustomerService customerService;
    @Mock private AppointmentValidator validator;
    @Mock private AppointmentMapper mapper;
    @Mock private AppointmentRepository repository;
    @Mock private PetValidator petValidator;
    @Mock private AccessValidator accessValidator;
    @InjectMocks private AppointmentService service;

    private AppointmentRequestDTO dto;
    private Customer customer;
    private Pet pet;
    private UUID appointmentId;
    private LocalDate reschedulingDate;
    private LocalTime reschedulingTime;
    private Appointment appointment;

    private AppointmentRequestDTO generateDTO() {
        return new AppointmentRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now().plusDays(1),
                LocalTime.of(14, 30),
                ServiceType.FULL_GROOMING,
                AppointmentStatus.SCHEDULED
        );
    }

    private Appointment generateAppointment(){
        return Appointment.builder()
                .id(UUID.fromString("b6daba9b-69e5-40eb-bf4b-eaab632c4ce9"))
                .customer(customer)
                .pet(pet)
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .appointmentType(ServiceType.FULL_GROOMING)
                .appointmentHour(LocalTime.of(13, 30))
                .appointmentDate(LocalDate.of(2025, 5, 25))
                .build();
    }

    @BeforeEach
    void setUp() {
        dto = generateDTO();
        customer = Customer.builder().id(dto.customerId()).name("John").build();
        pet = Pet.builder().id(dto.petId()).name("Rex").build();
        appointmentId = UUID.fromString("b6daba9b-69e5-40eb-bf4b-eaab632c4ce9");
        reschedulingDate = LocalDate.now().plusDays(2);
        reschedulingTime = LocalTime.now().plusHours(10);
        appointment = generateAppointment();

    }

    @Test
    void shouldScheduleAppointmentSuccessfully() {
        when(customerService.findById(dto.customerId())).thenReturn(customer);
        when(petService.findById(dto.petId())).thenReturn(pet);

        doNothing().when(validator).validateAppointmentTimeWindow(dto.appointmentDate(), dto.appointmentHour());
        doNothing().when(validator).checkForExistingAppointmentAtSameTime(dto.appointmentDate(), dto.appointmentHour());
        doNothing().when(petValidator).isPetOwner(pet, customer);
        doNothing().when(accessValidator).isOwnerOrAdmin(dto.customerId());

        when(mapper.toEntity(dto)).thenReturn(appointment);
        when(repository.save(any())).thenReturn(appointment);

        Appointment result = service.scheduleAppointment(dto);
        assertAll(
                () -> assertEquals(AppointmentStatus.SCHEDULED, result.getAppointmentStatus()),
                () -> assertEquals(customer, result.getCustomer()),
                () -> assertEquals(pet, result.getPet())
        );
        verify(repository).save(appointment);
    }

    @Test
    void shouldThrowIfAppointmentTimeIsAlreadyTaken() {
        when(customerService.findById(dto.customerId())).thenReturn(customer);
        when(petService.findById(dto.petId())).thenReturn(pet);

        doNothing().when(validator).validateAppointmentTimeWindow(dto.appointmentDate(), dto.appointmentHour());
        doThrow(new InvalidAppointmentException("There is already an appointment with this time"))
                .when(validator).checkForExistingAppointmentAtSameTime(
                        dto.appointmentDate(),
                        dto.appointmentHour()
                );

        InvalidAppointmentException ex = assertThrows(InvalidAppointmentException.class, () -> service.scheduleAppointment(dto));
        assertEquals("There is already an appointment with this time", ex.getMessage());

        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowIfPetDoesNotBelongToCustomer() {
        when(customerService.findById(dto.customerId())).thenReturn(customer);
        when(petService.findById(dto.petId())).thenReturn(pet);

        doNothing().when(validator).validateAppointmentTimeWindow(dto.appointmentDate(), dto.appointmentHour());
        doNothing().when(validator).checkForExistingAppointmentAtSameTime(dto.appointmentDate(), dto.appointmentHour());
        doThrow(new UnauthorizedException("This pet does not belong to this customer"))
                .when(petValidator).isPetOwner(pet, customer);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> service.scheduleAppointment(dto));
        assertEquals("This pet does not belong to this customer", ex.getMessage());

        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowIfScheduledTooSoon() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now(),
                LocalTime.now().plusHours(1),
                ServiceType.FULL_GROOMING,
                AppointmentStatus.SCHEDULED
        );

        doThrow(new InvalidAppointmentException("Appointment must be scheduled at least 10 hour in advance"))
                .when(validator).validateAppointmentTimeWindow(
                        request.appointmentDate(),
                        request.appointmentHour()
                );

        InvalidAppointmentException ex = assertThrows(InvalidAppointmentException.class, () -> service.scheduleAppointment(request));
        assertEquals("Appointment must be scheduled at least 10 hour in advance", ex.getMessage());
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowIfCustomerNotFound() {
        doThrow(new NotFoundException("Customer ID not found")).when(customerService).findById(dto.customerId());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.scheduleAppointment(dto));
        assertEquals("Customer ID not found", ex.getMessage());
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowIfPetNotFound() {
        doThrow(new NotFoundException("Pet ID not found")).when(petService).findById(dto.petId());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.scheduleAppointment(dto));
        assertEquals("Pet ID not found", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowIfCustomerIsNotAuthorized() {
        doNothing().when(validator).validateAppointmentTimeWindow(dto.appointmentDate(), dto.appointmentHour());
        doNothing().when(validator).checkForExistingAppointmentAtSameTime(dto.appointmentDate(), dto.appointmentHour());

        doThrow(new UnauthorizedException("User not authorized")).when(accessValidator).isOwnerOrAdmin(dto.customerId());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> service.scheduleAppointment(dto));
        assertEquals("User not authorized", ex.getMessage());
        verify(repository, never()).save(appointment);
    }


    @Test
    void shouldReturnAnAppointmentById(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(generateAppointment()));
        Appointment result = service.findAppointmentById(appointmentId);

        assertAll(
                () -> assertEquals(appointmentId, result.getId()),
                () -> assertEquals(customer, result.getCustomer()),
                () -> assertEquals(pet, result.getPet()),
                () -> assertEquals(AppointmentStatus.SCHEDULED, result.getAppointmentStatus()),
                () -> assertEquals(LocalTime.of(13, 30), result.getAppointmentHour()),
                () -> assertEquals(LocalDate.of(2025, 5, 25), result.getAppointmentDate())
        );
        verify(repository).findById(appointmentId);
    }

    @Test
    void shouldThrowExceptionWhenAppointmentIsNotFound() {
        doThrow(new NotFoundException("Appointment ID not found")).when(repository).findById(appointmentId);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.findAppointmentById(appointmentId));
        assertEquals("Appointment ID not found", ex.getMessage());

        verify(repository).findById(appointmentId);
    }

    @Test
    void shouldRescheduleAnAppointment(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(validator).validateAppointmentTimeWindow(reschedulingDate, reschedulingTime);
        doNothing().when(validator).checkForExistingAppointmentAtSameTime(reschedulingDate, reschedulingTime);
        doNothing().when(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());

        Appointment result = service.rescheduleAppointment(appointmentId, reschedulingDate, reschedulingTime);
        assertAll(
                () -> assertEquals(AppointmentStatus.RESCHEDULED, result.getAppointmentStatus()),
                () -> assertEquals(reschedulingDate, result.getAppointmentDate()),
                () -> assertEquals(reschedulingTime, result.getAppointmentHour())
        );
        verify(repository).save(result);
    }

    @Test
    void shouldThrowExceptionIfRescheduleTooSoon(){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().plusHours(1);

        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        doThrow(new InvalidAppointmentException("Appointment must be scheduled at least 10 hour in advance"))
                .when(validator).validateAppointmentTimeWindow(date, time);

        InvalidAppointmentException ex = assertThrows(InvalidAppointmentException.class,
                () -> service.rescheduleAppointment(appointmentId, date, time)
        );

        assertEquals("Appointment must be scheduled at least 10 hour in advance", ex.getMessage());
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowExceptionIfRescheduleIsDuplicate(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        doNothing().when(validator).validateAppointmentTimeWindow(reschedulingDate, reschedulingTime);
        doThrow(new InvalidAppointmentException("There is already an appointment with this time"))
                .when(validator).checkForExistingAppointmentAtSameTime(reschedulingDate, reschedulingTime);

        InvalidAppointmentException ex = assertThrows(InvalidAppointmentException.class,
                () -> service.rescheduleAppointment(appointmentId, reschedulingDate, reschedulingTime)
        );

        assertEquals("There is already an appointment with this time", ex.getMessage());

        verify(validator).checkForExistingAppointmentAtSameTime(reschedulingDate, reschedulingTime);
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowIfRescheduleCustomerIsNotAuthorized(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        doNothing().when(validator).validateAppointmentTimeWindow(reschedulingDate, reschedulingTime);
        doNothing().when(validator).checkForExistingAppointmentAtSameTime(reschedulingDate, reschedulingTime);
        doThrow(new UnauthorizedException("User not authorized")).when(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> service.rescheduleAppointment(appointmentId, reschedulingDate, reschedulingTime)
        );
        assertEquals("User not authorized", ex.getMessage());

        verify(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldCancelSuccessfulAppointment(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        doNothing().when(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());

        assertDoesNotThrow(() -> service.cancelAppointment(appointmentId));

        verify(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());
        verify(repository).findById(appointmentId);
        verify(repository).save(appointment);
    }

    @Test
    void shouldThrowExceptionWhenTryCancelAppointment(){
        doThrow(new NotFoundException("Appointment ID not found")).when(repository).findById(appointmentId);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.cancelAppointment(appointmentId));
        assertEquals("Appointment ID not found", ex.getMessage());

        verify(repository).findById(appointmentId);
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAuthorized(){
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        doThrow(new UnauthorizedException("User not authorized"))
                .when(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> service.cancelAppointment(appointmentId)
        );
        assertEquals("User not authorized", ex.getMessage());

        verify(repository).findById(appointmentId);
        verify(accessValidator).isOwnerOrAdmin(appointment.getCustomer().getId());
        verify(repository, never()).save(appointment);
    }

    @Test
    void shouldReturnListAppointmentsByCustomerId(){
        UUID customerId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(1, 2);

        Page<Appointment> mockAppointments = new PageImpl<>(
                List.of(generateAppointment(), appointment),
                pageRequest,
                2
        );

        when(repository.findAllByCustomerId(customerId, pageRequest)).thenReturn(mockAppointments);
        Page<AppointmentResponseDTO> result = service.listAllAppointmentsByCustomerId(customerId, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getSize());
        assertFalse(result.isEmpty());
        verify(repository).findAllByCustomerId(customerId, pageRequest);
    }

}
