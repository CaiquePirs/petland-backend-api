package com.petland.modules.petCare.controller;

import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/petcare/services")
@RequiredArgsConstructor
public class PetCareController {

    private final PetCareService petCareService;
    private final GeneratePetCareResponse petCareResponse;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PetCareResponseDTO> createService(@RequestBody @Valid PetCareRequestDTO requestDTO){
        PetCare petCare = petCareService.create(requestDTO);
        return ResponseEntity.ok().body(petCareResponse.generate(petCare));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PetCareResponseDTO> findServiceById(@PathVariable(name = "id") UUID petCareID){
        PetCare petCare = petCareService.findServiceById(petCareID);
        return ResponseEntity.ok().body(petCareResponse.generate(petCare));
    }

}
