package com.petland.modules.customer.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.appointment.dtos.AppointmentResponseDTO;
import com.petland.modules.consultation.dtos.ConsultationHistoryResponseDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Customer Profile", description = "Endpoints for managing authenticated customer profiles")
public interface CustomerProfileApi {

    @Operation(summary = "Get Profile", description = "Get the profile of the logged in customer ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<CustomerResponseDTO> getMyProfile();


    @Operation(summary = "Get My Pets", description = "List the customer pets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets list found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<List<PetResponseDTO>> getMyPets();


    @Operation(summary = "Get My Sales History", description = "List the customer sales history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale list found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Page<SaleResponseDTO>> getMySalesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);


    @Operation(summary = "Get My Services History", description = "List the customer service history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service list found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Page<PetCareHistoryResponseDTO>> getMyServicesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);


    @Operation(summary = "Get My Consultations History", description = "List the customer consultations history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultations list found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Page<ConsultationHistoryResponseDTO>> getMyConsultationsHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);


    @Operation(summary = "Get My Appointments History", description = "List the customer appointments history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments list found successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authorized",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "User not authorized",
                      "errors": [
                        { "field": "Not Authorized", "message": "User not authorized" }
                      ]
                    }
                """))),
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
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Page<AppointmentResponseDTO>> getMyAppointmentsHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);



}
