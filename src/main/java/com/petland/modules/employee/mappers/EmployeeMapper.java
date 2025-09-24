package com.petland.modules.employee.mappers;

import com.petland.modules.employee.controller.dto.EmployeeRequestDTO;
import com.petland.modules.employee.controller.dto.EmployeeResponseDTO;
import com.petland.modules.employee.controller.dto.EmployeeResponseReportDTO;
import com.petland.modules.employee.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDTO employeeRequestDTO);
    EmployeeResponseDTO toDTO(Employee employee);
    EmployeeResponseReportDTO toReports(Employee employee);
}
