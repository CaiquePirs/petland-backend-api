package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.dtos.ReportsResponseDTO;
import com.petland.modules.dashboard.service.SalesReports;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard/reports")
@RequiredArgsConstructor
public class SaleReportController {

    private final SalesReports saleReport;

    @GetMapping("/sales/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportsResponseDTO> salesReportByPeriod(
            @RequestParam(value = "dateMin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMin,
            @RequestParam(value = "dateMax", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax){

        ReportsResponseDTO salesReport = saleReport.totalSalesByPeriod(dateMin, dateMax);
        return ResponseEntity.ok().body(salesReport);
    }

    @GetMapping("/sales/by-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportsResponseDTO> salesReportsByProduct(@PathVariable(name = "id") UUID productId){
        ReportsResponseDTO salesReport = saleReport.totalSalesByProductId(productId);
        return ResponseEntity.ok().body(salesReport);
    }
}
