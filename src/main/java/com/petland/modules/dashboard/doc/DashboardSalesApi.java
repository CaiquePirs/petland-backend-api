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

@Tag(name = "Sales Dashboard", description = "Endpoints for the managing sales reports")
public interface DashboardSalesApi {

    @Operation(summary = "Sales Report", description = "Issue sales reports by period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sales reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Sales reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateSalesReportByPeriod(
            @RequestParam(value = "dateMin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMin,
            @RequestParam(value = "dateMax", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateMax);


    @Operation(summary = "Sale Report", description = "Issue sales reports by product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report issue successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sales reports not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Sales reports not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Report> generateSalesReportByProduct(@PathVariable(name = "id") UUID productId);
}
