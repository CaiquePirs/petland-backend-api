package com.petland.modules.employee.mappers;

import com.petland.modules.employee.dto.EmployeeRequestDTO;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDTO employeeRequestDTO);
    EmployeeResponseDTO toDTO(Employee employee);
}
