package com.petland.modules.appointment.generator;

import com.petland.modules.appointment.model.Appointment;

public interface PdfFileGenerator {
    byte[] generate(Appointment appointment);
}
