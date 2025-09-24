package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.controller.doc.DashboardVaccinationsApi;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.VaccinationsReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard/reports/vaccinations")
@RequiredArgsConstructor
public class DashboardVaccinationsController implements DashboardVaccinationsApi {

    private final VaccinationsReportsService generator;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateReportVaccinationsByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax) {
        Report report = generator.totalByPeriod(dateMin, dateMax);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/by-vaccine/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateReportVaccinationsByVaccine(@PathVariable(name = "id") UUID vaccineId) {
        Report report = generator.totalByVaccine(vaccineId);
        return ResponseEntity.ok((report));
    }
}
