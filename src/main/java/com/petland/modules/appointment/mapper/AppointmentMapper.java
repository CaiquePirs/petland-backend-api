package com.petland.modules.appointment.mapper;

import com.petland.modules.appointment.controller.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.controller.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toEntity(AppointmentRequestDTO dto);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "pet.id", target = "petId")
    AppointmentResponseDTO toDTO(Appointment appointment);

}
