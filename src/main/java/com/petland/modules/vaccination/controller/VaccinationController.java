package com.petland.modules.vaccination.controller;

import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService vaccinationService;
    private final GenerateVaccinationResponse generateVaccinationResponse;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> register(@RequestBody VaccinationRequestDTO vaccinationRequestDTO){
        Vaccination vaccination = vaccinationService.register(vaccinationRequestDTO);
        VaccinationResponseDTO vaccinationResponseDTO = generateVaccinationResponse.generate(vaccination);
        return ResponseEntity.ok().body(vaccinationResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> getById(@PathVariable(name = "id") UUID vaccinationId){
        Vaccination vaccination = vaccinationService.findById(vaccinationId);
        VaccinationResponseDTO vaccinationResponse = generateVaccinationResponse.generate(vaccination);
        return ResponseEntity.ok().body(vaccinationResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID vaccinationId){
     vaccinationService.deleteById(vaccinationId);
     return ResponseEntity.noContent().build();
    }

}
