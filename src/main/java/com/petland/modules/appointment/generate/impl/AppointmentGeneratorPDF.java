package com.petland.modules.appointment.generate.impl;

import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.generate.GenerateAppointmentPDF;
import com.petland.modules.dashboard.reports.pdf.PDFGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentGeneratorPDF implements PDFGenerator<Appointment> {

    private final GenerateAppointmentPDF generateAppointmentPDF;

    public byte[] generate(Appointment appointment) {
        return generateAppointmentPDF.issue(appointment);
    }
}
