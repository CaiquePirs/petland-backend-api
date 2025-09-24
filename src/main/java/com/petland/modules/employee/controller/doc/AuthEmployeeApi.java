package com.petland.modules.employee.controller.doc;

import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.employee.controller.dto.EmployeeRequestDTO;
import com.petland.modules.employee.controller.dto.EmployeeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Employee Authentication", description = "endpoints to managing employee authentication")
public interface AuthEmployeeApi {

    @Operation(summary = "Employee Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successfully"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid Credentials",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 401,
                      "message": "Invalid Credentials",
                      "errors": [
                        { "field": "Credentials", "message": "Email/password incorrect" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO);

    @Operation(summary = "Employee Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee registered successfully"),
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
                            { "field": "name", "message": "Name is required" },
                            { "field": "email", "message": "Email is required" },
                            { "field": "email", "message": "Email must be valid" },
                            { "field": "password", "message": "Password is required" },
                            { "field": "phone", "message": "Phone is required" },
                            { "field": "department", "message": "Department is required" },
                            { "field": "address", "message": "Address is required" },
                            { "field": "hireDate", "message": "Hire date is required" },
                            { "field": "dateBirth", "message": "Date birth is required" }
                          ]
                        }
                    """)
                    )),
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
    ResponseEntity<EmployeeResponseDTO> register(@RequestBody @Valid EmployeeRequestDTO employeeRequestDTO);

}
