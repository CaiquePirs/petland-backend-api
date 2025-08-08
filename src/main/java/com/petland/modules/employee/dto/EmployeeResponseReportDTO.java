package com.petland.modules.employee.dto;

import com.petland.modules.employee.enums.Department;

import java.util.UUID;

public record EmployeeResponseReportDTO(UUID id,
                                        String name,
                                        Department department) {
}
