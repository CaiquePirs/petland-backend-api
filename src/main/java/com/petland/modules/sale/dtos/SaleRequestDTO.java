package com.petland.modules.sale.dtos;

import com.petland.modules.consultation.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record SaleRequestDTO(@NotNull(message = "Customer Id is required")
                             UUID customerId,

                             @NotNull(message = "An item of a sale must be informed in the requisition")
                             List<ItemsSaleRequestDTO> itemsSaleRequestDTO,

                             @NotNull(message = "Payment type is required")
                             PaymentType paymentType) {
}
