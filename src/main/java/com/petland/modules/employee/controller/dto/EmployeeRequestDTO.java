package com.petland.modules.employee.controller.dto;

import com.petland.modules.employee.model.enums.Department;
import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to create a new employee")
public record EmployeeRequestDTO(
        @NotBlank(message = "Name is required")
        @Schema(description = "Full name of the employee", example = "John Smith")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Employee's email address", example = "john.smith@email.com")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(description = "Employee's password", example = "P@ssword123")
        String password,

        @NotBlank(message = "Phone is required")
        @Schema(description = "Contact phone number", example = "+15551234567")
        String phone,

        @NotNull(message = "Department is required")
        @Schema(description = "Employee's department", example = "FINANCIAL")
        Department department,

        @NotNull(message = "Address is required")
        @Schema(description = "Employee's full address")
        Address address,

        @NotNull(message = "Hire date is required")
        @Schema(description = "Date when the employee was hired", example = "2023-01-01")
        LocalDate hireDate,

        @NotNull(message = "Date birth is required")
        @Schema(description = "Employee's date of birth", example = "1990-05-12")
        LocalDate dateBirth
) {}

