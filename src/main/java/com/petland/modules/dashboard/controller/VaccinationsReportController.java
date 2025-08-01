package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.dtos.ReportsResponseDTO;
import com.petland.modules.dashboard.service.VaccinationsReports;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard/reports")
@RequiredArgsConstructor
public class VaccinationsReportController {

    private final VaccinationsReports generator;

    @GetMapping("vaccinations/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportsResponseDTO> vaccinationsReportByPeriod(@RequestParam(required = false) LocalDate dateMin,
                                                                         @RequestParam(required = false) LocalDate dateMax){
        var reports = generator.totalBilledPerPeriod(dateMin, dateMax);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("vaccinations/by-vaccine/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportsResponseDTO> vaccinationsReportByVaccine(@PathVariable(name = "id") UUID vaccineId){
        var reports = generator.totalBilledByVaccine(vaccineId);
        return ResponseEntity.ok(reports);
    }
}
