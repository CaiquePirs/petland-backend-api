package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.BillingReportsService;
import com.petland.modules.dashboard.strategies.impl.SendReportViaPDF;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard/reports")
@RequiredArgsConstructor
public class DashboardBillingController {

    private final BillingReportsService billingReport;
    private final SendReportViaPDF sendReportViaPDF;


    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateTotalRevenueReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax) {

        Report report = billingReport.generate(dateMin, dateMax);
        return ResponseEntity.ok().body(report);
    }

    @GetMapping("/by-pdf")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<byte[]> generateTotalRevenueReportByPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax) {

        Report report = billingReport.generate(dateMin, dateMax);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                .body(sendReportViaPDF.generate(report));
    }
}
