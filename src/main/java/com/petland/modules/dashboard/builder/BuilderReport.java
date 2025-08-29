package com.petland.modules.dashboard.builder;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.modules.dashboard.report.Report;
import com.petland.modules.employee.mappers.EmployeeMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Builder
public class BuilderReport {

    private final EmployeeMapper mapper;
    private final AccessValidator access;

    public Report generate(BigDecimal totalBilled, BigDecimal totalProfit, Integer sumItemsSold, BigDecimal totalCostOperating){
        return Report.builder()
                .totalRevenue(totalBilled)
                .totalProfit(totalProfit)
                .itemsQuantity(sumItemsSold)
                .operatingCost(totalCostOperating)
                .issueDate(LocalDateTime.now())
                .employee(mapper.toReports(access.getEmployeeLogged()))
                .build();
    }
}
