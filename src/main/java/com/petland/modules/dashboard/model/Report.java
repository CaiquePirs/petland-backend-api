package com.petland.modules.dashboard.model;

import com.petland.modules.employee.controller.dto.EmployeeResponseReportDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Represents a financial and operational report related to employee performance and services.")
public class Report {

    @Schema(description = "Total revenue generated.", example = "15000.75")
    private BigDecimal totalRevenue;

    @Schema(description = "Total number of items included in the report.", example = "125")
    private Integer itemsQuantity;

    @Schema(description = "Total operating cost associated with the report.", example = "5000.50")
    private BigDecimal operatingCost;

    @Schema(description = "Total profit calculated in the report.", example = "10000.25")
    private BigDecimal totalProfit;

    @Schema(description = "Date and time when the report was issued.", example = "2025-09-07T12:30:00")
    private LocalDateTime issueDate;

    @Schema(description = "Employee associated with this report.")
    private EmployeeResponseReportDTO employee;
}

