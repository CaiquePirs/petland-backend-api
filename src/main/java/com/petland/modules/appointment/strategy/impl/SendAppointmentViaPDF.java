package com.petland.modules.appointment.strategy.impl;

import com.petland.modules.appointment.strategy.AppointmentStrategy;
import com.petland.modules.appointment.strategy.generate.GenerateAppointmentPDF;
import com.petland.modules.appointment.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendAppointmentViaPDF implements AppointmentStrategy<byte []> {

    private final GenerateAppointmentPDF generateAppointment;

    public byte[] generate(Appointment appointment) {
        return generateAppointment.issue(appointment);
    }
}
