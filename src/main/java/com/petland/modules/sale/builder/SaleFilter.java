package com.petland.modules.sale.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SaleFilter {

    private Long employeeId;
    private Long customerId;
    private PaymentType paymentType;
    private BigDecimal totalSalesMin;
    private BigDecimal totalSalesMax;
    private StatusEntity status;

    public String getStatus(){
        return status.toString().toUpperCase();
    }

}
