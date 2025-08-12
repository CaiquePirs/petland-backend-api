package com.petland.modules.sale.controller;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.util.GenerateSaleResponse;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable(name = "id") UUID saleId){
        Sale sale = saleService.findSaleById(saleId);
        SaleResponseDTO saleResponse = generateSaleResponse.generateSaleResponse(sale);
        return ResponseEntity.ok(saleResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateSaleById(@PathVariable(name = "id") UUID saleId){
        saleService.deactivateSaleById(saleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SaleResponseDTO>> findSalesByCustomerId(
            @PathVariable(name = "id") UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Page<SaleResponseDTO> salesList = saleService.findSalesByCustomerId(
                customerId, PageRequest.of(page, size)
        );

        return ResponseEntity.ok(salesList);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SaleResponseDTO>> findAllSalesByFilter(
            @RequestParam(value = "employeeId", required = false) UUID employeeId,
            @RequestParam(value = "customerId", required = false) UUID customerId,
            @RequestParam(value = "paymentType", required = false) PaymentType paymentType,
            @RequestParam(value = "totalSalesMin", required = false) BigDecimal totalSalesMin,
            @RequestParam(value = "totalSalesMin", required = false) BigDecimal totalSalesMax,
            @RequestParam(value = "status", defaultValue = "ACTIVE")StatusEntity status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Page<SaleResponseDTO> salesList = saleService.findAllSalesByFilter(employeeId, customerId, paymentType, totalSalesMin,
                totalSalesMax, status ,PageRequest.of(page, size)
        );
        return ResponseEntity.ok(salesList);
    }

}
