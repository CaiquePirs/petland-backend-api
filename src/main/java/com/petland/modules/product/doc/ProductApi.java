package com.petland.modules.product.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.product.builder.ProductFilter;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.dto.ProductResponseDTO;
import com.petland.modules.product.dto.ProductUpdateDTO;
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

@Tag(name = "Product", description = "Endpoints for managing products")
public interface ProductApi {

    @Operation(summary = "Product Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product registered successfully"),
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
                        { "field": "description", "message": "Description is required" },
                        { "field": "brand", "message": "Brand is required" },
                        { "field": "supplierName", "message": "Supplier name is required" },
                        { "field": "productType", "message": "Product type is required" },
                        { "field": "manufactureDate", "message": "Manufacture date is required" },
                        { "field": "expirationDate", "message": "Expiration date is required" },
                        { "field": "costPrice", "message": "Cost price is required" },
                        { "field": "costSale", "message": "Cost sale is required" },
                        { "field": "stockQuantity", "message": "Stock quantity is required" }
                      ]
                    }
                """)))
    })
    ResponseEntity<ProductResponseDTO> register(@RequestBody @Valid ProductRequestDTO productRequestDTO);


    @Operation(summary = "Find Product By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully"),
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
                        { "field": "Not Found", "message": "Product ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<ProductResponseDTO> findProductById(@PathVariable(name = "id") UUID productId);

    @Operation(summary = "Deactivate Product By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deactivate successfully"),
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
                        { "field": "Not Found", "message": "Product ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Void> deactivateProductById(@PathVariable(name = "id") UUID productId);

    @Operation(summary = "List All Customer By Filter")
    @ApiResponse(responseCode = "200")
    ResponseEntity<Page<ProductResponseDTO>> findAllProductsByFilter(
            @ModelAttribute ProductFilter filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size);

    @Operation(summary = "Update Product By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),

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
                        { "field": "Not Found", "message": "Product ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<ProductResponseDTO> updateProductById(
            @PathVariable(name = "id") UUID productId,
            @RequestBody ProductUpdateDTO productDTO);

}
