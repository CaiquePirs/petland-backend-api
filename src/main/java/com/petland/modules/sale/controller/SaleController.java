package com.petland.modules.sale.controller;

import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.GenerateSaleResponse;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final GenerateSaleResponse generateSaleResponse;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO saleRequestDTO){
        Sale sale = saleService.registerSale(saleRequestDTO);
        SaleResponseDTO saleResponseDTO = generateSaleResponse.generateSaleResponse(sale);
        return ResponseEntity.ok().body(saleResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable(name = "id") UUID saleId){
        Sale sale = saleService.findSaleById(saleId);
        SaleResponseDTO saleResponse = generateSaleResponse.generateSaleResponse(sale);
        return ResponseEntity.ok().body(saleResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID saleId){
        saleService.deleteSaleById(saleId);
        return ResponseEntity.noContent().build();
    }
}
