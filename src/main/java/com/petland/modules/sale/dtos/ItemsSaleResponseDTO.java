package com.petland.modules.sale.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ItemsSaleResponseDTO {
    private UUID id;
    private UUID productId;
    private int productQuantity;
    private BigDecimal itemsSaleTotal;
    private UUID saleId;
}
