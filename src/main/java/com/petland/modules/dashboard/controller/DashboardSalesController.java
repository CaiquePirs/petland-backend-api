package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.SalesReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard/reports/sales")
@RequiredArgsConstructor
public class DashboardSalesController {

    private final SalesReportsService saleReport;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateSalesReportByPeriod(
            @RequestParam(value = "dateMin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMin,
            @RequestParam(value = "dateMax", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax){
        Report report = saleReport.totalByPeriod(dateMin, dateMax);
        return ResponseEntity.ok().body(report);
    }

    @GetMapping("/by-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateSalesReportByProduct(@PathVariable(name = "id") UUID productId){
        Report report = saleReport.totalByProductId(productId);
        return ResponseEntity.ok().body(report);
    }
}
