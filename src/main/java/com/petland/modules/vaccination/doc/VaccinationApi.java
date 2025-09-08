package com.petland.modules.vaccination.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.vaccination.builder.VaccinationFilter;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Vaccination", description = "Endpoints to manage vaccinations")
public interface VaccinationApi {

    @Operation(summary = "Vaccination Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaccination registered successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Customer ID not found" },
                        { "field": "Not Found", "message": "Pet ID not found" },
                        { "field": "Not Found", "message": "Employee ID not found" }
                        { "field": "Not Found", "message": "Vaccine ID not found" },
                      ]
                    }
                """)
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "Unauthorized",
                      "errors": [
                        { "field": "Unauthorized", "message": "This pet does not belong to the customer" },
                        { "field": "Unauthorized", "message": "User not authorized" }
                      ]
                    }
                """)
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Error Vaccination",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Stock Insufficient",
                      "errors": [
                        { "field": "Stock Insufficient", "message": "Vaccine stock is insufficient" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<VaccinationResponseDTO> register(@RequestBody VaccinationRequestDTO vaccinationRequestDTO);

    @Operation(summary = "Find Vaccination By ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vaccination found successfully"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Vaccination ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Vaccination ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<VaccinationResponseDTO> findVaccinationById(@PathVariable(name = "id") UUID vaccinationId);


    @Operation(summary = "Deactivate Vaccination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vaccination deactivate successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Vaccination ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Vaccination ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<Void> deactivateVaccinationById(@PathVariable(name = "id") UUID vaccinationId);

    @Operation(summary = "Update Vaccination By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccination updated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Vaccination ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<VaccinationResponseDTO> updateVaccinationById(@RequestBody @Valid VaccinationUpdateDTO dto, @PathVariable(name = "id") UUID vaccinationId);

    @Operation(summary = "List All Vaccinations")
    ResponseEntity<Page<VaccinationResponseDTO>> listAllVaccinationsByFilter(@ModelAttribute VaccinationFilter filter, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size);

}
