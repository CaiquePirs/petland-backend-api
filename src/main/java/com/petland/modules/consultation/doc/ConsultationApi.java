package com.petland.modules.consultation.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.consultation.builder.ConsultationFilter;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.enums.ConsultationStatus;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Consultation", description = "Endpoints to manage consultations")
public interface ConsultationApi {

    @Operation(summary = "Consultation Register")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Consultation registered successfully",
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
                        { "field": "Not Found", "message": "Employee ID not found" },
                        { "field": "Not Found", "message": "Product ID not found" },
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
                        { "field": "Unauthorized", "message": "The sale must be registered to the same customer as the one associated with the consultation."},
                        { "field": "Unauthorized", "message": "This pet does not belong to the customer" },
                        { "field": "Unauthorized", "message": "User not authorized" }
                      ]
                    }
                """)
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Error Consultation",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Stock Insufficient",
                      "errors": [
                        { "field": "Stock Insufficient", "message": "Product stock is insufficient" },
                        { "field": "Stock Insufficient", "message": "Vaccine stock is ins" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<ConsultationResponseDTO> registerConsultation(@RequestBody @Valid ConsultationRequestDTO requestDTO);

    @Operation(summary = "Find Consultation By ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consultation found successfully",
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
                      "message": "Consultation ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Consultation ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<ConsultationResponseDTO> findConsultationById(@PathVariable(name = "id") UUID consultationId);

    @Operation(summary = "List all consultations")
    ResponseEntity<Page<ConsultationResponseDTO>> findAllConsultationsByFilter(
            @ModelAttribute ConsultationFilter filter,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) int size);

    @Operation(summary = "Deactivate Consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consultation deactivate successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Consultation ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Consultation ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<Void> deactivateConsultationById(@PathVariable(name = "id") UUID consultationId);


    @Operation(summary = "Toggle Consultation Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status consultation toggle successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Consultation ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Consultation ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<Void> toggleStatusConsultation(@PathVariable(name = "id") UUID consultationId,
                                                  @RequestParam(name = "status") ConsultationStatus status);


}
