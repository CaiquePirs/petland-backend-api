package com.petland.modules.dashboard.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.dashboard.report.Report;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Vaccinations Dashboard", description = "Endpoints for the managing vaccinations reports")
public interface DashboardVaccinationsApi {

    @Operation(summary = "Vaccinations Report", description = "Issue vaccinations reports by period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vaccinations reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Vaccinations reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateReportVaccinationsByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMax);


    @Operation(summary = "Vaccinations Report", description = "Issue vaccinations reports by vaccine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vaccinations reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Vaccinations reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateReportVaccinationsByVaccine(@PathVariable(name = "id") UUID vaccineId);
}
