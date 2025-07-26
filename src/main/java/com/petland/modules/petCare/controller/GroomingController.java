package com.petland.modules.petCare.controller;

import com.petland.modules.petCare.dtos.GroomingRequestDTO;
import com.petland.modules.petCare.dtos.GroomingResponseDTO;
import com.petland.modules.petCare.mappers.GroomingMapper;
import com.petland.modules.petCare.model.Grooming;
import com.petland.modules.petCare.service.GroomingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/groomings")
@RequiredArgsConstructor
public class GroomingController {

    private final GroomingService groomingService;
    private final GroomingMapper groomingMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroomingResponseDTO> register(@RequestBody @Valid GroomingRequestDTO groomingRequestDTO){
        Grooming grooming = groomingService.create(groomingRequestDTO);
        return ResponseEntity.ok().body(groomingMapper.toDTO(grooming));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroomingResponseDTO> getById(@PathVariable(name = "id") UUID groomingId){
        Grooming grooming = groomingService.findById(groomingId);
        return ResponseEntity.ok().body(groomingMapper.toDTO(grooming));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID groomingId){
        groomingService.deleteById(groomingId);
        return ResponseEntity.noContent().build();
    }

}
