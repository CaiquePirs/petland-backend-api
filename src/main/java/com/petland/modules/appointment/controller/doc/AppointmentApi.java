package com.petland.modules.appointment.controller.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.appointment.builder.AppointmentFilter;
import com.petland.modules.appointment.controller.dtos.AppointmentRequestDTO;
import com.petland.modules.appointment.controller.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.controller.dtos.AppointmentUpdateDTO;
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

@Tag(name = "Appointment", description = "Endpoints to manage appointments")
public interface AppointmentApi {

    @Operation(summary = "Schedule a new appointment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment scheduled successfully",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
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
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not found",
                      "errors": [
                        { "field": "Not found", "message": "Customer ID not found" },
                        { "field": "Not found", "message": "Pet ID not found" }
                      ]
                    }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Error Appointment",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Error Appointment",
                      "errors": [
                        { "field": "Error Appointment", "message": "Appointment must be scheduled at least 10 hours in advance" },
                        { "field": "Error Appointment", "message": "Appointment cannot be scheduled more than 30 days in advance" },
                        { "field": "Error Appointment", "message": "There is already an appointment with this time" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<byte[]> scheduleAppointment(@RequestBody @Valid AppointmentRequestDTO dto);


    @Operation(summary = "Find appointment by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment found",
                    content = @Content(schema = @Schema(implementation = AppointmentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment ID not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not found",
                      "errors": [
                        { "field": "Not found", "message": "Appointment ID not found" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<AppointmentResponseDTO> findAppointmentById(@PathVariable(name = "id") UUID appointmentId);


    @Operation(summary = "Reschedule Appointment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment scheduled successfully",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment ID not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not found",
                      "errors": [
                        { "field": "Not found", "message": "Appointment ID not found" }
                      ]
                    }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Error Appointment",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Error Appointment",
                      "errors": [
                        { "field": "Error Appointment", "message": "Appointment must be scheduled at least 10 hours in advance" },
                        { "field": "Error Appointment", "message": "Appointment cannot be scheduled more than 30 days in advance" },
                        { "field": "Error Appointment", "message": "There is already an appointment with this time" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<byte[]> rescheduleAppointmentById(
            @PathVariable(name = "id") UUID appointmentId,
            @RequestBody @Valid AppointmentUpdateDTO requestDTO
    );


    @Operation(summary = "Cancel Appointment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment canceled successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment ID not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not found",
                      "errors": [
                        { "field": "Not found", "message": "Appointment ID not found" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<Void> cancelAppointmentById(@PathVariable(name = "id") UUID appointmentId);


    @Operation(summary = "Toggle Appointment Status by ID", description = "Status updated successfully")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment status updated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment ID not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not found",
                      "errors": [
                        { "field": "Not found", "message": "Appointment ID not found" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<Void> toggleStatusAppointmentById(
            @PathVariable(name = "id") UUID appointmentId,
            @RequestParam(name = "status") String status
    );


    @Operation(summary = "List all appointments")
    ResponseEntity<Page<AppointmentResponseDTO>> listAllAppointments(
            @ModelAttribute AppointmentFilter filter,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size
    );
}