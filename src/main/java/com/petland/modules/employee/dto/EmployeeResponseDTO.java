package com.petland.modules.employee.dto;

import com.petland.modules.employee.enums.Department;
import com.petland.common.entity.Address;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record EmployeeResponseDTO(UUID id,
                                  String name,
                                  String email,
                                  String phone,
                                  Department department,
                                  Address address,
                                  LocalDate hireDate,
                                  LocalDate dateBirth){
}
