package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.service.PetCareReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard/reports/pet-care")
@RequiredArgsConstructor
public class DashboardPetCareController {

    private final PetCareReportsService reportsService;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> findTotalRevenueByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax){

        Report report = reportsService.totalServicesByPeriod(dateMin, dateMax);
        return ResponseEntity.ok().body(report);
    }
}
