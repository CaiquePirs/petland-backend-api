package com.petland.modules.dashboard.controller;

import com.petland.modules.dashboard.controller.doc.DashboardPetCareApi;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.dashboard.service.PetCareReportsService;
import com.petland.modules.petCare.model.enums.PetCareType;
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
public class DashboardPetCareController implements DashboardPetCareApi {

    private final PetCareReportsService service;

    @GetMapping("/by-period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateServiceReportByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax){

        Report report = service.totalByPeriod(dateMin, dateMax);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/by-service-type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> generateServiceReportByType(@RequestParam(value = "type", required = false) PetCareType petCareType){
            Report report = service.totalByServiceType(petCareType);
            return ResponseEntity.ok(report);
    }
}
