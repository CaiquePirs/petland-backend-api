package com.petland.modules.sale.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Item Sale", description = "Endpoints for managing items from sales")
public interface ItemSaleApi {

    @Operation(summary = "Find Item Sale By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item sale found successfully"),
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
                        { "field": "Not Found", "message": "Item sale ID not found" },
                        { "field": "Not Found", "message": "Sale ID not found" }, 
                        { "field": "Not Found", "message": "ItemSale with ID not found in Sale ID" }
                      ]
                    }
                """)))
    })
    ResponseEntity<ItemsSaleResponseDTO> findItemById(@PathVariable(name = "saleId") UUID saleId, @PathVariable(name = "id") UUID itemId);

    @Operation(summary = "Deactivate Item Sale By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item sale deactivate successfully"),
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
                        { "field": "Not Found", "message": "Item sale ID not found" },
                        { "field": "Not Found", "message": "Sale ID not found" }, 
                        { "field": "Not Found", "message": "ItemSale with ID not found in Sale ID" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Void> deactivateItemById(@PathVariable(name = "saleId") UUID saleId, @PathVariable(name = "id") UUID itemId);

}
