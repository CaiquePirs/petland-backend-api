package com.petland.modules.vaccination.controller;

import com.petland.modules.vaccination.builder.VaccineFilter;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.dto.VaccineUpdateDTO;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.VaccineService;
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
@RequestMapping("/vaccines")
@RequiredArgsConstructor
public class VaccineController {

    private final VaccineService service;
    private final VaccineMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineResponseDTO> create(@RequestBody @Valid VaccineRequestDTO vaccineRequestDTO){
        Vaccine vaccine = service.create(vaccineRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(vaccine));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineResponseDTO> findVaccineById(@PathVariable(name = "id") UUID vaccineId){
        Vaccine vaccine = service.findById(vaccineId);
        return ResponseEntity.ok(mapper.toDTO(vaccine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateVaccineById(@PathVariable(name = "id") UUID vaccineId){
        service.deactivateById(vaccineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VaccineResponseDTO>> findAllVaccinesByFilter(
            @ModelAttribute VaccineFilter filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<VaccineResponseDTO> vaccinesListPage = service.filterAllVaccinesByFilter(
               filter, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(vaccinesListPage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VaccineResponseDTO> updateVaccineByID(@PathVariable(name = "id") UUID vaccineId,
                                                                @RequestBody VaccineUpdateDTO dto){
        Vaccine vaccine = service.updateById(vaccineId, dto);
        return ResponseEntity.ok(mapper.toDTO(vaccine));
    }
}
