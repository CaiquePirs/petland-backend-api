package com.petland.modules.petCare.controller;

import com.petland.modules.petCare.dtos.BathRequestDTO;
import com.petland.modules.petCare.dtos.BathResponseDTO;
import com.petland.modules.petCare.mappers.BathMapper;
import com.petland.modules.petCare.model.Bath;
import com.petland.modules.petCare.service.BathService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/baths")
@RequiredArgsConstructor
public class BathController {

    private final BathService bathService;
    private final BathMapper bathMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BathResponseDTO> register(@RequestBody @Valid BathRequestDTO bathRequestDTO){
        Bath bath = bathService.create(bathRequestDTO);
        return ResponseEntity.ok().body(bathMapper.toDTO(bath));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BathResponseDTO> getById(@PathVariable(name = "id")UUID bathId){
        Bath bath = bathService.findById(bathId);
        return ResponseEntity.ok().body(bathMapper.toDTO(bath));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID bathId){
        bathService.deleteById(bathId);
        return ResponseEntity.noContent().build();
    }
}
