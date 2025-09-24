package com.petland.modules.employee.controller.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.employee.builder.EmployeeFilter;
import com.petland.modules.employee.controller.dto.EmployeeResponseDTO;
import com.petland.modules.employee.controller.dto.EmployeeUpdateDTO;
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

@Tag(name = "Employee", description = "Endpoints for managing employers")
public interface EmployeeApi {

    @Operation(summary = "Get Profile", description = "Get the profile of the logged in employee ")
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
                        { "field": "Not Found", "message": "Employee ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<EmployeeResponseDTO> getProfile();

    @Operation(summary = "Find Employee By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
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
                        { "field": "Not Found", "message": "Employee ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<EmployeeResponseDTO> findEmployeeById(@PathVariable(name = "id") UUID employeeId);


    @Operation(summary = "Deactivate Employee By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deactivate successfully"),
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
                        { "field": "Not Found", "message": "Employee ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Void> deactivateEmployeeById(@PathVariable(name = "id") UUID customerId);


    @Operation(summary = "List All Customer By Filter")
    @ApiResponse(responseCode = "200")
    ResponseEntity<Page<EmployeeResponseDTO>> findAllEmployeeByFilter(
            @ModelAttribute EmployeeFilter filter,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);


    @Operation(summary = "Update Employee By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
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
                        { "field": "Not Found", "message": "Employee ID not found" }
                      ]
                    }
                """))),

            @ApiResponse(
                    responseCode = "409",
                    description = "Email found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Email Found",
                      "errors": [
                        { "field": "Credentials", "message": "This email already exist" }
                      ]
                    }
                """)))
    })
    ResponseEntity<EmployeeResponseDTO> updateEmployeeById(@PathVariable(name = "id") UUID employeeId,
                                                           @RequestBody @Valid EmployeeUpdateDTO employeeDTO);

}
