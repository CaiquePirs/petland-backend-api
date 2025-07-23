package com.petland.modules.vaccination.controller;

import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vaccination")
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
}
