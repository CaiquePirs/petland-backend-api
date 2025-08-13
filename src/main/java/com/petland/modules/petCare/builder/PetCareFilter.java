package com.petland.modules.petCare.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCareFilter {

    private String petId;
    private String customerId;
    private String employeeId;
    private BigDecimal minRevenue;
    private BigDecimal maxCostOperating;
    private BigDecimal minProfit;
    private BigDecimal maxProfit;
    private StatusEntity status;
    private LocalDate startDate;
    private LocalDate endDate;

    public UUID getPetId() {
        return petId != null && !petId.isBlank() ? UUID.fromString(petId) : null;
    }

    public UUID getCustomerId() {
        return customerId != null && !customerId.isBlank() ? UUID.fromString(customerId) : null;
    }

    public UUID getEmployeeId() {
        return employeeId != null && !employeeId.isBlank() ? UUID.fromString(employeeId) : null;
    }
}
