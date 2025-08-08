package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.reports.BillingReport;
import com.petland.modules.dashboard.service.BillingReportsService;
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

    private final BillingReport billingReport;
    private final BillingReportsService reportsService;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> findTotalRevenueByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax) {

        Report report = billingReport.generate(dateMin, dateMax);
        return ResponseEntity.ok().body(report);
    }

    @GetMapping("/by-pdf")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<byte[]> findTotalRevenueByPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("ReportsSale.pdf").build());

        var reportPDF = reportsService.issueBillingReportByPDF(dateMin, dateMax);
        return new ResponseEntity<>(reportPDF, headers, HttpStatus.OK);
    }
}
