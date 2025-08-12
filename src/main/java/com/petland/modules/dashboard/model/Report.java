package com.petland.modules.dashboard.model;

import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private BigDecimal totalRevenue;
    private Integer itemsQuantity;
    private BigDecimal operatingCost;
    private BigDecimal totalProfit;
    private LocalDateTime issueDate;
    private EmployeeResponseReportDTO employee;
}

