package com.petland.modules.appointment.generator.impl;

import com.petland.modules.appointment.generator.PdfFileGenerator;
import com.petland.modules.appointment.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendAppointmentViaPDF {

    private final PdfFileGenerator pdfFileGenerator;

    public byte[] generate(Appointment appointment) {
        return pdfFileGenerator.generate(appointment);
    }
}
