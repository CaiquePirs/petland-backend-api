package com.petland.modules.sale.controller.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "DTO used to inform a single item in a sale")
public record ItemsSaleRequestDTO(
        @NotNull(message = "Product Id is required")
        @Schema(description = "ID of the product being sold", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID productId,

        @NotNull(message = "Product sales quantity must be informed")
        @Schema(description = "Quantity of the product being sold", example = "2")
        Integer productQuantity
) {}
