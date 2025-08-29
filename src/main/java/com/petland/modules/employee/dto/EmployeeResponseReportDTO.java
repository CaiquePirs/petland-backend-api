package com.petland.modules.employee.dto;

import com.petland.modules.employee.enums.Department;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EmployeeResponseReportDTO(UUID id,
                                        String name,
                                        Department department) {
}
