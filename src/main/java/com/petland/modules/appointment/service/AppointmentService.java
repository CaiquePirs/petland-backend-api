package com.petland.modules.appointment.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.appointment.builder.AppointmentFilter;
import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.mapper.AppointmentMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.specifications.AppointmentSpecifications;
import com.petland.modules.appointment.validator.AppointmentValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final PetService petService;
    private final CustomerService customerService;
    private final AppointmentValidator validator;
    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;
    private final PetValidator petValidator;
    private final AccessValidator accessValidator;

    public Appointment scheduleAppointment(AppointmentRequestDTO requestDTO){
        Pet pet = petService.findById(requestDTO.petId());
        Customer customer = customerService.findById(requestDTO.customerId());

        validator.validateAppointmentTimeWindow(requestDTO.appointmentDate(), requestDTO.appointmentHour());
        validator.checkForExistingAppointmentAtSameTime(requestDTO.appointmentDate(), requestDTO.appointmentHour());
        petValidator.isPetOwner(pet, customer);
        accessValidator.isOwnerOrAdmin(requestDTO.customerId());

        Appointment appointment = mapper.toEntity(requestDTO);
        appointment.setAppointmentStatus(requestDTO.appointmentStatus() != null ? requestDTO.appointmentStatus() : AppointmentStatus.SCHEDULED);
        appointment.setCustomer(customer);
        appointment.setPet(pet);
        return repository.save(appointment);
    }

    public Appointment findAppointmentById(UUID appointmentId){
        return repository.findById(appointmentId)
                .filter(a -> !a.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Appointment ID not found"));
    }

    public Appointment rescheduleAppointment(UUID appointmentId, LocalDate appointmentDate, LocalTime appointmentHour){
      Appointment appointment = findAppointmentById(appointmentId);

      validator.validateAppointmentTimeWindow(appointmentDate, appointmentHour);
      validator.checkForExistingAppointmentAtSameTime(appointmentDate, appointmentHour);
      accessValidator.isOwnerOrAdmin(appointment.getCustomer().getId());

      appointment.setAppointmentStatus(AppointmentStatus.RESCHEDULED);
      appointment.setAppointmentDate(appointmentDate);
      appointment.setAppointmentHour(appointmentHour);
      return repository.save(appointment);
    }

    public void cancelAppointment(UUID appointmentId){
        Appointment appointment = findAppointmentById(appointmentId);
        accessValidator.isOwnerOrAdmin(appointment.getCustomer().getId());

        appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
        repository.save(appointment);
    }

    public void toggleStatusAppointment(UUID appointmentId, String status){
        Appointment appointment = findAppointmentById(appointmentId);

        if(status.equals(AppointmentStatus.CANCELED.toString())){
            cancelAppointment(appointmentId);
        }

        appointment.setAppointmentStatus(AppointmentStatus.valueOf(status));
        repository.save(appointment);
    }

    public Page<AppointmentResponseDTO> listAllAppointmentsByFilter(AppointmentFilter filter, Pageable pageable){
        return repository.findAll(AppointmentSpecifications.specification(filter), pageable)
                .map(mapper::toDTO);
    }

    public Page<AppointmentResponseDTO> listAllAppointmentsByCustomerId(UUID customerId, Pageable pageable){
        List<AppointmentResponseDTO> listAppointments = repository.findAllByCustomerId(customerId, pageable)
                .stream()
                .filter(a -> !a.getStatus().equals(StatusEntity.DELETED))
                .map(mapper::toDTO)
                .toList();
        return new PageImpl<>(listAppointments, pageable, listAppointments.size());
    }
}
