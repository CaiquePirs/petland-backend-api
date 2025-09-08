package com.petland.modules.sale.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.sale.builder.SaleFilter;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Sale", description = "Endpoints for managing sales")
public interface SaleApi {

    @Operation(summary = "Sale Register")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sale registered successfully",
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
                        { "field": "Not Found", "message": "Product ID not found" }
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
                    description = "Error Sale",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "message": "Stock Insufficient",
                      "errors": [
                        { "field": "Stock Insufficient", "message": "Product stock is insufficient" }
                      ]
                    }
                """)
                    )
            )
    })
    ResponseEntity<SaleResponseDTO> create(@RequestBody SaleRequestDTO saleRequestDTO);

    @Operation(summary = "Find Sale By ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale found successfully",
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
                      "message": "Sale ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Sale ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable(name = "id") UUID saleId);

    @Operation(summary = "Deactivate Sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sale deactivate successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Sale ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Sale ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<Void> deactivateSaleById(@PathVariable(name = "id") UUID saleId);

    @Operation(summary = "List Sales By Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List sales found successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Customer ID not found",
                      "errors": [
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """))
            ),
    })
    ResponseEntity<Page<SaleResponseDTO>> findSalesByCustomerId(@PathVariable(name = "id") UUID customerId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size);

    @Operation(summary = "List all Sales")
    ResponseEntity<Page<SaleResponseDTO>> findAllSalesByFilter(@ModelAttribute SaleFilter filter, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size);

}
