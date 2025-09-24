package com.petland.dashboard;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.dashboard.builder.BuilderReport;
import com.petland.modules.dashboard.model.Report;
import com.petland.modules.employee.controller.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.model.enums.Department;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BuilderReportTest {

    @Mock private EmployeeMapper mapper;
    @Mock private AccessValidator access;
    @InjectMocks private BuilderReport builderReport;

    private Employee employee;
    private EmployeeResponseReportDTO employeeDTO;

    private BigDecimal totalBilled;
    private BigDecimal totalProfit;
    private Integer sumItemsSold;
    private BigDecimal totalCostOperating;

    @BeforeEach
    void setUp(){
        employee = Employee.builder().department(Department.MANAGER).id(UUID.randomUUID()).name("Employee").build();
        employeeDTO = EmployeeResponseReportDTO.builder().department(employee.getDepartment()).id(employee.getId()).name(employee.getName()).build();
        totalBilled = BigDecimal.valueOf(2000);
        totalProfit = BigDecimal.valueOf(400);
        sumItemsSold = 30;
        totalCostOperating = BigDecimal.valueOf(300);
    }

    @Test
    void shouldCreateReportSuccessfully(){
        when(access.getEmployeeLogged()).thenReturn(employee);
        when(mapper.toReports(employee)).thenReturn(employeeDTO);

        Report reportResult = builderReport.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating);
        assertNotNull(reportResult);

        assertAll(
                () -> assertEquals(totalBilled, reportResult.getTotalRevenue()),
                () -> assertInstanceOf(LocalDateTime.class, reportResult.getIssueDate()),
                () -> assertEquals(totalProfit, reportResult.getTotalProfit()),
                () -> assertEquals(sumItemsSold, reportResult.getItemsQuantity()),
                () -> assertEquals(totalCostOperating, reportResult.getOperatingCost()),
                () -> assertEquals(employeeDTO, reportResult.getEmployee())
        );

        verify(access).getEmployeeLogged();
        verify(mapper).toReports(employee);
    }

    @Test
    void shouldThrowExceptionWhenCreateReports(){
        doThrow(new NotFoundException("Employer not found")).when(access).getEmployeeLogged();

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> builderReport.generate(totalBilled, totalProfit, sumItemsSold, totalCostOperating)
        );
        assertEquals("Employer not found", ex.getMessage());
        verify(mapper, never()).toReports(employee);
    }
}
