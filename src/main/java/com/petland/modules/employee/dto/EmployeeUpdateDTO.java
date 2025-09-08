package com.petland.modules.employee.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import com.petland.common.entity.enums.Roles;
import com.petland.modules.employee.enums.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update an employee partially")
public record EmployeeUpdateDTO(
        @Schema(description = "Full name of the employee", example = "John Smith")
        String name,

        @Email(message = "Email must be valid")
        @Schema(description = "Employee's email address", example = "john.smith@email.com")
        String email,

        @Schema(description = "Employee's password", example = "P@ssword123")
        String password,

        @Schema(description = "Contact phone number", example = "+15551234567")
        String phone,

        @Schema(description = "Employee's department", example = "FINANCIAL")
        Department department,

        @Schema(description = "Hire date of the employee", example = "2023-01-01")
        LocalDate hireDate,

        @Schema(description = "Date of birth", example = "1990-05-12")
        LocalDate dateBirth,

        @Schema(description = "Updated address of the employee")
        UpdateAddressDTO addressDTO,

        @Schema(description = "Employee's role", example = "ADMIN")
        Roles role
) {}

