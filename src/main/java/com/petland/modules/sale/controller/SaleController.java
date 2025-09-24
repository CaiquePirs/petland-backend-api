package com.petland.modules.sale.controller;

import com.petland.modules.sale.builder.SaleFilter;
import com.petland.modules.sale.controller.doc.SaleApi;
import com.petland.modules.sale.controller.dtos.SaleRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.mappers.SaleMapperGenerator;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController implements SaleApi {

    private final SaleService service;
    private final SaleMapperGenerator response;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO saleRequestDTO){
        Sale sale = service.registerSale(saleRequestDTO);
        SaleResponseDTO saleResponseDTO = response.generateSaleResponse(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable(name = "id") UUID saleId){
        Sale sale = service.findSaleById(saleId);
        SaleResponseDTO saleResponse = response.generateSaleResponse(sale);
        return ResponseEntity.ok(saleResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateSaleById(@PathVariable(name = "id") UUID saleId){
        service.deactivateSaleById(saleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SaleResponseDTO>> findSalesByCustomerId(
            @PathVariable(name = "id") UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Page<SaleResponseDTO> sales = service.findSalesByCustomerId(
                customerId, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(sales);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SaleResponseDTO>> findAllSalesByFilter(
            @ModelAttribute SaleFilter filter,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Page<SaleResponseDTO> sales = service.findAllSalesByFilter(
                filter ,PageRequest.of(page, size)
        );
        return ResponseEntity.ok(sales);
    }

}
