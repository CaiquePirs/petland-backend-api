package com.petland.modules.dashboard.dtos;

import com.petland.modules.employee.dto.EmployeeResponseReportDTO;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record Report(BigDecimal totalRevenue,
                     Integer itemsQuantity,
                     BigDecimal operatingCost,
                     BigDecimal totalProfit,
                     LocalDateTime issueDate,
                     EmployeeResponseReportDTO employee){}

