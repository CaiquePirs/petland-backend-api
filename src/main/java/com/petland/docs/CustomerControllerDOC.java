package com.petland.docs;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.customer.builder.CustomerFilter;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.dto.UpdateCustomerDTO;
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

@Tag(name = "Customer", description = "Endpoints for managing customers")
public interface CustomerControllerDOC {

    @Operation(summary = "Find Customer By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found successfully"),
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
    ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable(name = "id") UUID customerId);

    @Operation(summary = "Deactivate Customer By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deactivate successfully"),
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
    ResponseEntity<Void> deactivateCustomerById(@PathVariable(name = "id") UUID customerId);


    @Operation(summary = "List All Customer By Filter")
    ResponseEntity<Page<CustomerResponseDTO>> listAllCustomersByFilter(
            @ModelAttribute CustomerFilter filter,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size);


    @Operation(summary = "Update Customer By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),

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
    ResponseEntity<CustomerResponseDTO> updateCustomerById(
            @PathVariable(name = "id") UUID customerId,
            @RequestBody @Valid UpdateCustomerDTO updateCustomerDTO);




}
