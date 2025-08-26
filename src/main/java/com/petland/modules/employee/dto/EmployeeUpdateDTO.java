package com.petland.modules.employee.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import com.petland.common.entity.enums.Roles;
import com.petland.modules.employee.enums.Department;
import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeUpdateDTO(String name,
                                @Email(message = "Email must be valid")
                                String email,
                                String password,
                                String phone,
                                Department department,
                                LocalDate hireDate,
                                LocalDate dateBirth,
                                UpdateAddressDTO addressDTO,
                                Roles role) {
}
