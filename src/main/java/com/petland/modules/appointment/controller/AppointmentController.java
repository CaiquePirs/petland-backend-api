package com.petland.modules.appointment.controller;

import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.service.AppointmentService;
import com.petland.modules.appointment.generate.impl.AppointmentGeneratorPDF;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;
    private final AppointmentGeneratorPDF generatorPDF;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<byte []> scheduleAppointment(@RequestBody @Valid AppointmentRequestDTO dto){
        Appointment appointment = service.scheduleAppointment(dto);
        byte[] appointmentScheduledPDF = generatorPDF.generate(appointment);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=appointment.pdf")
                .body(appointmentScheduledPDF);
    }
}
