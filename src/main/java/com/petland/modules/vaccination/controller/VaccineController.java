package com.petland.modules.vaccination.controller;

import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.VaccineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vaccines")
@RequiredArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;
    private final VaccineMapper vaccineMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineResponseDTO> create(@RequestBody @Valid VaccineRequestDTO vaccineRequestDTO){
        Vaccine vaccine = vaccineService.create(vaccineRequestDTO);
        return ResponseEntity.ok().body(vaccineMapper.toDTO(vaccine));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineResponseDTO> findById(@PathVariable(name = "id") UUID vaccineId){
        Vaccine vaccine = vaccineService.findById(vaccineId);
        return ResponseEntity.ok().body(vaccineMapper.toDTO(vaccine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(UUID vaccineId){
        vaccineService.deleteById(vaccineId);
        return ResponseEntity.noContent().build();
    }


}
