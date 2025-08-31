package com.petland.modules.sale.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ItemsSaleRequestDTO(@NotNull(message = "Product Id is required")
                                  UUID productId,

                                  @NotNull(message = "Product sales quantity must be informed")
                                  Integer productQuantity) {
}
