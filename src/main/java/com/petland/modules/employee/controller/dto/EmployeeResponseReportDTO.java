package com.petland.modules.employee.controller.dto;

import com.petland.modules.employee.model.enums.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "DTO used for employee reports")
public record EmployeeResponseReportDTO(
        @Schema(description = "Unique ID of the employee", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Full name of the employee", example = "Julia Smith")
        String name,

        @Schema(description = "Department to which the employee belongs", example = "FINANCIAL")
        Department department
) {}


