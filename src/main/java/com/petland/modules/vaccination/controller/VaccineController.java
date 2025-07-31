package com.petland.modules.vaccination.controller;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.enums.VaccineType;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.VaccineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID vaccineId){
        vaccineService.deleteById(vaccineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VaccineResponseDTO>> getVaccines(@RequestParam(required = false) String lotNumber,
                                                                @RequestParam(required = false) String supplierName,
                                                                @RequestParam(required = false) VaccineType vaccineType,
                                                                @RequestParam(required = false) BigDecimal minPurchasePrice,
                                                                @RequestParam(required = false) BigDecimal maxPurchasePrice,
                                                                @RequestParam(required = false) BigDecimal minPriceSale,
                                                                @RequestParam(required = false) BigDecimal maxPriceSale,
                                                                @RequestParam(required = false) Integer minStockQuantity,
                                                                @RequestParam(required = false) Integer maxStockQuantity,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate manufactureAfter,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationBefore,
                                                                @RequestParam(required = false, defaultValue = "ACTIVE") StatusEntity status,
                                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "10") int size) {
        var vaccinesListPage = vaccineService.filterAllVaccinesByFilter(
                lotNumber, supplierName, vaccineType, minPurchasePrice,
                maxPurchasePrice, minPriceSale, maxPriceSale, minStockQuantity,
                maxStockQuantity, manufactureAfter, expirationBefore, status, PageRequest.of(page, size)
        );
        return ResponseEntity.ok().body(vaccinesListPage);
    }



}
