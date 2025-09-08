package com.petland.modules.petCare.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.petCare.builder.PetCareFilter;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "PetCare", description = "Endpoints for managing petcare services")
public interface PetCareApi {

    @Operation(summary = "Petcare Register")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Petcare registered successfully",
                    content = @Content(schema = @Schema(implementation = ConsultationResponseDTO.class))
            ),

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
                    ))
    })
    ResponseEntity<PetCareResponseDTO> createService(@RequestBody @Valid PetCareRequestDTO requestDTO);

    @Operation(summary = "Find Petcare By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PetCare found successfully"),
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
                        { "field": "Not Found", "message": "Petcare ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<PetCareResponseDTO> findServiceById(@PathVariable(name = "id") UUID petCareID);

    @Operation(summary = "Deactivate Petcare By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Petcare deactivate successfully"),
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
                        { "field": "Not Found", "message": "Petcare ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<PetCareResponseDTO> deactivateServiceById(@PathVariable(name = "id") UUID petCareID);

    @Operation(summary = "List All Petcare By Filter")
    @ApiResponse(responseCode = "200")
    ResponseEntity<Page<PetCareResponseDTO>> findAllServicesByFilter(
            @ModelAttribute PetCareFilter filter,
            @RequestParam(required = false, name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") @Min(1) int size
    );

}
