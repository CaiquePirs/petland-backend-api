package com.petland.modules.petCare.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PetCareFilter {

    private UUID petId;
    private UUID customerId;
    private UUID employeeId;
    private BigDecimal minRevenue;
    private BigDecimal maxCostOperating;
    private BigDecimal minProfit;
    private BigDecimal maxProfit;
    private StatusEntity status;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
