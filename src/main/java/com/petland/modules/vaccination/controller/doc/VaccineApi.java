package com.petland.modules.vaccination.controller.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.vaccination.builder.VaccineFilter;
import com.petland.modules.vaccination.controller.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.controller.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.controller.dto.VaccineUpdateDTO;
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

@Tag(name = "Vaccine", description = "Endpoints for managing vaccines")
public interface VaccineApi {

    @Operation(summary = "Vaccine Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaccine registered successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 400,
                      "message": "Validation failed",
                      "errors": [
                        { "field": "lotNumber", "message": "Lot number is required" },
                        { "field": "supplierName", "message": "Supplier name is required" },
                        { "field": "vaccineType", "message": "Vaccine Type is required" },
                        { "field": "purchasePrice", "message": "Purchase price is required" },
                        { "field": "priceSale", "message": "Price sale is required" },
                        { "field": "stockQuantity", "message": "Stock Quantity is required" },
                        { "field": "manufactureDate", "message": "Manufacture Date is required" },
                        { "field": "expirationDate", "message": "Expiration Date is required" }
                      ]
                    }
                """)))
    })
    ResponseEntity<VaccineResponseDTO> create(@RequestBody @Valid VaccineRequestDTO vaccineRequestDTO);

    @Operation(summary = "Find Vaccine By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine found successfully"),
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
                        { "field": "Not Found", "message": "Vaccine ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<VaccineResponseDTO> findVaccineById(@PathVariable(name = "id") UUID vaccineId);

    @Operation(summary = "Deactivate Vaccine By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vaccine deactivate successfully"),
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
                        { "field": "Not Found", "message": "Vaccine ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Void> deactivateVaccineById(@PathVariable(name = "id") UUID vaccineId);


    @Operation(summary = "List All Vaccines")
    ResponseEntity<Page<VaccineResponseDTO>> findAllVaccinesByFilter(@ModelAttribute VaccineFilter filter, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size);

    @Operation(summary = "Update Vaccine By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaccine updated successfully"),
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
                        { "field": "Not Found", "message": "Vaccine ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<VaccineResponseDTO> updateVaccineByID(@PathVariable(name = "id") UUID vaccineId, @RequestBody VaccineUpdateDTO dto);

}
