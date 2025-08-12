package com.petland.modules.petCare.controller;

import com.petland.modules.petCare.builder.PetCareFilter;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        PetCare petCare = petCareService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(petCareResponse.generate(petCare));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PetCareResponseDTO> findServiceById(@PathVariable(name = "id") UUID petCareID){
        PetCare petCare = petCareService.findById(petCareID);
        return ResponseEntity.ok(petCareResponse.generate(petCare));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PetCareResponseDTO> deactivateServiceById(@PathVariable(name = "id") UUID petCareID){
        petCareService.deactivateById(petCareID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PetCareResponseDTO>> findAllServicesByFilter(
            @ModelAttribute PetCareFilter filter,
            @RequestParam(required = false, name = "page", defaultValue = "0")@Min(0) int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") @Min(1) int size){

        Page<PetCareResponseDTO> listServices = petCareService.findAllByFilter(filter, PageRequest.of(page, size));
        return ResponseEntity.ok(listServices);
    }

}
