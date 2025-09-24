package com.petland.modules.vaccination.controller;

import com.petland.modules.vaccination.builder.VaccinationFilter;
import com.petland.modules.vaccination.controller.doc.VaccinationApi;
import com.petland.modules.vaccination.controller.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.controller.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.controller.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.mappers.VaccinationMapperGenerator;
import com.petland.modules.vaccination.service.VaccinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vaccinations")
@RequiredArgsConstructor
public class VaccinationController implements VaccinationApi {

    private final VaccinationService service;
    private final VaccinationMapperGenerator response;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> register(@RequestBody VaccinationRequestDTO vaccinationRequestDTO){
        Vaccination vaccination = service.register(vaccinationRequestDTO);
        VaccinationResponseDTO vaccinationResponseDTO = response.generate(vaccination);
        return ResponseEntity.status(HttpStatus.CREATED).body(vaccinationResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> findVaccinationById(@PathVariable(name = "id") UUID vaccinationId){
        Vaccination vaccination = service.findById(vaccinationId);
        VaccinationResponseDTO vaccinationResponse = response.generate(vaccination);
        return ResponseEntity.ok(vaccinationResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateVaccinationById(@PathVariable(name = "id") UUID vaccinationId){
     service.deactivateById(vaccinationId);
     return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccinationResponseDTO> updateVaccinationById(
            @RequestBody @Valid VaccinationUpdateDTO dto,
            @PathVariable(name = "id") UUID vaccinationId){

        Vaccination vaccination = service.updateById(dto, vaccinationId);
        return ResponseEntity.ok(response.generate(vaccination));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VaccinationResponseDTO>> listAllVaccinationsByFilter(
            @ModelAttribute VaccinationFilter filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size){

        Page<VaccinationResponseDTO> vaccinationList = service.listAllVaccinationsByFilter(
                filter, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(vaccinationList);
    }

}
