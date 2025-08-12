package com.petland.modules.vaccination.controller;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.service.VaccinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(vaccinationResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> findVaccinationById(@PathVariable(name = "id") UUID vaccinationId){
        Vaccination vaccination = vaccinationService.findById(vaccinationId);
        VaccinationResponseDTO vaccinationResponse = generateVaccinationResponse.generate(vaccination);
        return ResponseEntity.ok(vaccinationResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateVaccinationById(@PathVariable(name = "id") UUID vaccinationId){
     vaccinationService.deactivateById(vaccinationId);
     return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> updateVaccinationById(
            @RequestBody @Valid VaccinationUpdateDTO dto,
            @PathVariable(name = "id") UUID vaccinationId){

        Vaccination vaccination = vaccinationService.updateById(dto, vaccinationId);
        return ResponseEntity.ok(generateVaccinationResponse.generate(vaccination));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VaccinationResponseDTO>> listAllVaccinationsByFilter(
            @RequestParam(required = false) UUID petId, @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID veterinarianId,
            @RequestParam(required = false, defaultValue = "ACTIVE") StatusEntity status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate vaccinationDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextDoseBefore,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size){

        Page<VaccinationResponseDTO> vaccinationList = vaccinationService.listAllVaccinationsByFilter(
                petId, customerId, veterinarianId, vaccinationDate, nextDoseBefore, status, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(vaccinationList);
    }

}
