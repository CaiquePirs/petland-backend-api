package com.petland.modules.petCare.controller;

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
        return ResponseEntity.ok().body(petCareResponse.generate(petCare));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PetCareResponseDTO> findServiceById(@PathVariable(name = "id") UUID petCareID){
        PetCare petCare = petCareService.findById(petCareID);
        return ResponseEntity.ok().body(petCareResponse.generate(petCare));
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
            @RequestParam(required = false) UUID petId,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) BigDecimal minRevenue,
            @RequestParam(required = false) BigDecimal maxCostOperating,
            @RequestParam(required = false) BigDecimal minProfit,
            @RequestParam(required = false) BigDecimal maxProfit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") @Min(1) int size){

        Page<PetCareResponseDTO> listServices = petCareService.findAllByFilter(petId, customerId, employeeId,
                minRevenue, maxCostOperating, minProfit,
                maxProfit, startDate, endDate, PageRequest.of(page, size));

        return ResponseEntity.ok().body(listServices);
    }

}
