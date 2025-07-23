package com.petland.modules.employee.dto;

import com.petland.modules.employee.enums.Department;
import com.petland.common.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmployeeRequestDTO(@NotBlank(message = "Name is required")
                                 String name,

                                 @Email(message = "Email must be valid")
                                 @NotBlank(message = "Email is required")
                                 String email,

                                 @NotBlank(message = "Password is required")
                                 String password,

                                 @NotBlank(message = "Phone is required")
                                 String phone,

                                 @NotNull(message = "Department is required")
                                 Department department,

                                 @Valid
                                 Address address,

                                 @NotNull(message = "Hire date is required")
                                 LocalDate hireDate,

                                 @NotNull(message = "Date birth is required")
                                 LocalDate dateBirth) {
}
