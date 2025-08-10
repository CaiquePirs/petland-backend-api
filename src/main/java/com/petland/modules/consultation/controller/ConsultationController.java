package com.petland.modules.consultation.controller;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.generate.GenerateConsultationResponse;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService service;
    private final GenerateConsultationResponse generate;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConsultationResponseDTO> registerConsultation(@RequestBody @Valid ConsultationRequestDTO requestDTO){
        Consultation consultation = service.registerConsultation(requestDTO);
        ConsultationResponseDTO responseDTO = generate.generateResponse(consultation);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConsultationResponseDTO> findConsultationById(@PathVariable(name = "id") UUID consultationId){
        Consultation consultation = service.findById(consultationId);
        return ResponseEntity.ok().body(generate.generateResponse(consultation));
    }

}
