package com.petland.modules.appointment.mapper;

import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.model.Appointment;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toEntity(AppointmentRequestDTO dto);
    AppointmentResponseDTO toDTO(Appointment appointment);

}
