package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.dtos.Report;
import com.petland.modules.dashboard.service.VaccinationsReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard/reports/vaccinations")
@RequiredArgsConstructor
public class DashboardVaccinationsController {

    private final VaccinationsReportsService generator;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> vaccinationsReportByPeriod(@RequestParam(required = false) LocalDate dateMin,
                                                             @RequestParam(required = false) LocalDate dateMax){
        var reports = generator.totalBilledByPeriod(dateMin, dateMax);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/by-vaccine/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> vaccinationsReportByVaccine(@PathVariable(name = "id") UUID vaccineId){
        var reports = generator.totalBilledByVaccine(vaccineId);
        return ResponseEntity.ok(reports);
    }
}
