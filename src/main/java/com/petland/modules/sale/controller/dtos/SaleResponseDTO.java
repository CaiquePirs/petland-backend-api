package com.petland.modules.sale.controller.dtos;

import com.petland.modules.consultation.model.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response DTO representing a sale transaction.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponseDTO {

    @Schema(description = "Unique identifier of the sale.", example = "c7f1a9e3-2b4d-4c6f-8e12-abcdef123456")
    private UUID id;

    @Schema(description = "Unique identifier of the employee who made the sale.", example = "e1a2b3c4-d5f6-7g8h-9i0j-123abc456def")
    private UUID employeeId;

    @Schema(description = "Unique identifier of the customer associated with the sale.", example = "a2b3c4d5-e6f7-8g9h-0i1j-987654321abc")
    private UUID customerId;

    @Schema(description = "List of items included in the sale.")
    private List<ItemsSaleResponseDTO> itemsSaleResponseDTO;

    @Schema(description = "Payment type used for the sale.", implementation = PaymentType.class, example = "CREDIT_CARD")
    private PaymentType paymentType;

    @Schema(description = "Total amount of the sale.", example = "250.75")
    private BigDecimal totalSales;
}
