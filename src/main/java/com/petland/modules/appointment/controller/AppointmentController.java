package com.petland.modules.appointment.controller;

import com.petland.modules.appointment.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.dtos.AppointmentUpdateDTO;
import com.petland.modules.appointment.mapper.AppointmentMapper;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.service.AppointmentService;
import com.petland.modules.appointment.generate.impl.AppointmentGeneratorPDF;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;
    private final AppointmentGeneratorPDF generatorPDF;
    private final AppointmentMapper mapper;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> findAppointmentById(@PathVariable(name = "id") UUID appointmentId){
        Appointment appointment = service.findAppointmentById(appointmentId);
        return ResponseEntity.ok().body(mapper.toDTO(appointment));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<byte []> rescheduleAppointmentById(
            @PathVariable(name = "id" ) UUID appointmentId,
            @RequestBody @Valid AppointmentUpdateDTO requestDTO){

        Appointment appointment = service.rescheduleAppointment(appointmentId, requestDTO.appointmentDate(), requestDTO.appointmentHour());
        byte[] appointmentScheduledPDF = generatorPDF.generate(appointment);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=appointment.pdf")
                .body(appointmentScheduledPDF);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> cancelAppointmentById(@PathVariable(name = "id") UUID appointmentId){
        service.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> toggleStatusAppointmentById(@PathVariable(name = "id") UUID appointmentId,
                                                            @RequestParam(name = "status") String status){
        service.toggleStatusAppointment(appointmentId, status.toUpperCase());
        return ResponseEntity.noContent().build();
    }
}
