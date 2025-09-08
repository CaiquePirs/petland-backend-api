package com.petland.modules.dashboard.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.petCare.enums.PetCareType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "PetCare Services Dashboard", description = "Endpoints for the managing petcare reports")
public interface DashboardPetCareApi {


    @Operation(summary = "Petcare Report", description = "Issue petcare reports by period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Petcare services reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Petcare services reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateServiceReportByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax);


    @Operation(summary = "Petcare Report", description = "Issue petcare reports by service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Petcare services reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Petcare services reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateServiceReportByType(@RequestParam(value = "type", required = false) PetCareType petCareType);



}
