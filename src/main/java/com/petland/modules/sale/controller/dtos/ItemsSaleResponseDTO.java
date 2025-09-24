package com.petland.modules.sale.controller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO used for item details in a sale response")
public class ItemsSaleResponseDTO {

    @Schema(description = "Unique ID of the item in the sale", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "ID of the product", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;

    @Schema(description = "Quantity of the product sold", example = "2")
    private int productQuantity;

    @Schema(description = "Total price of the item in the sale", example = "100.00")
    private BigDecimal itemsSaleTotal;

    @Schema(description = "ID of the sale this item belongs to", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID saleId;
}
