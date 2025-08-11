package com.petland.modules.appointment.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.mapper.AppointmentMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.validator.AppointmentValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
        appointment.setStatus(StatusEntity.DELETED);
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

}
