package com.petland.modules.appointment.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.mapper.AppointmentMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.AppointmentStatus;
import com.petland.modules.appointment.repository.AppointmentRepository;
import com.petland.modules.appointment.validator.AppointmentValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        validator.validateAppointmentTimeWindow(requestDTO);
        petValidator.isPetOwner(pet, customer);
        accessValidator.isOwnerOrAdmin(requestDTO.customerId());

        Appointment appointment = mapper.toEntity(requestDTO);
        appointment.setAppointmentStatus(requestDTO.appointmentStatus() != null ? requestDTO.appointmentStatus() : AppointmentStatus.SCHEDULED);
        appointment.setCustomer(customer);
        appointment.setPet(pet);
        return repository.save(appointment);
    }
}
