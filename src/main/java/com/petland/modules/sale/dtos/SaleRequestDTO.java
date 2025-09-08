package com.petland.modules.sale.dtos;

import com.petland.modules.consultation.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "DTO used to create a sale")
public record SaleRequestDTO(
        @NotNull(message = "Customer Id is required")
        @Schema(description = "ID of the customer making the purchase", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID customerId,

        @NotNull(message = "An item of a sale must be informed in the requisition")
        @Schema(description = "List of items being sold in the sale")
        List<ItemsSaleRequestDTO> itemsSaleRequestDTO,

        @NotNull(message = "Payment type is required")
        @Schema(description = "Payment type used for the sale", example = "CREDIT_CARD")
        PaymentType paymentType
) {}
