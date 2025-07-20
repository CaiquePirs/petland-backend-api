package com.petland.modules.sale.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemsSaleRequestDTO(@NotNull(message = "Product Id is required")
                                  UUID productId,

                                  @NotNull(message = "Product sales quantity must be informed")
                                  int productQuantity) {
}
