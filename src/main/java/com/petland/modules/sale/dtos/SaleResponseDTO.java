package com.petland.modules.sale.dtos;

import com.petland.modules.attendance.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SaleResponseDTO{
    private UUID id;
    private UUID employeeId;
    private UUID customerId;
    private List<ItemsSaleResponseDTO> itemsSaleResponseDTO;
    private PaymentType paymentType;
    private BigDecimal totalSales;
}
