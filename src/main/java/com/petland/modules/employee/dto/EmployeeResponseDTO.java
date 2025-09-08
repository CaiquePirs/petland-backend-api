package com.petland.modules.employee.dto;

import com.petland.modules.employee.enums.Department;
import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Schema(description = "DTO used for complete employee response")
public record EmployeeResponseDTO(
        @Schema(description = "Unique ID of the employee", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Full name of the employee", example = "John Smith")
        String name,

        @Schema(description = "Employee's email address", example = "john.smith@email.com")
        String email,

        @Schema(description = "Contact phone number", example = "+15551234567")
        String phone,

        @Schema(description = "Employee's department", example = "FINANCIAL")
        Department department,

        @Schema(description = "Employee's full address")
        Address address,

        @Schema(description = "Hire date", example = "2023-01-01")
        LocalDate hireDate,

        @Schema(description = "Date of birth", example = "1990-05-12")
        LocalDate dateBirth
) {}
