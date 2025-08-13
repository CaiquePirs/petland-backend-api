package com.petland.modules.sale.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleFilter {

    private String employeeId;
    private String customerId;
    private PaymentType paymentType;
    private BigDecimal totalSalesMin;
    private BigDecimal totalSalesMax;
    private StatusEntity status;

    public UUID getEmployeeId() {
        return employeeId != null && !employeeId.isBlank() ? UUID.fromString(employeeId) : null;
    }

    public UUID getCustomerId() {
        return customerId != null && !customerId.isBlank() ? UUID.fromString(customerId) : null;
    }
}
