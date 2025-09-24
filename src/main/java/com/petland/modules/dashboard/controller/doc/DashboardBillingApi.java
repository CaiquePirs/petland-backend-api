package com.petland.modules.dashboard.controller.doc;

import com.petland.modules.dashboard.model.Report;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Billing Dashboard", description = "Endpoints to manage pet shop billing reports")
public interface DashboardBillingApi {

    @Operation(summary = "Billing Reports", description = "Issue billing report by period for all services and sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully")
    })
    ResponseEntity<Report> generateTotalRevenueReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax);


    @Operation(summary = "Billing Reports", description = "Issue billing report by period for all services and sales by PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
                    ))
    })
    ResponseEntity<byte[]> generateTotalRevenueReportByPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax);
}
