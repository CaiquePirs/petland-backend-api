package com.petland.modules.sale.controller;

import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.GenerateResponse;
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
    private final GenerateResponse generateResponse;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO saleRequestDTO){
        Sale sale = saleService.registerSale(saleRequestDTO);
        SaleResponseDTO saleResponseDTO = generateResponse.generateSaleResponse(sale);
        return ResponseEntity.ok().body(saleResponseDTO);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable(name = "id") UUID saleId){
        Sale sale = saleService.findSaleById(saleId);
        SaleResponseDTO saleResponse = generateResponse.generateSaleResponse(sale);
        return ResponseEntity.ok().body(saleResponse);
    }


}
