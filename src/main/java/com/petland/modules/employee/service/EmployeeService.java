package com.petland.modules.employee.service;

import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.Roles;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.builder.EmployeeFilter;
import com.petland.modules.employee.controller.dto.EmployeeRequestDTO;
import com.petland.modules.employee.controller.dto.EmployeeResponseDTO;
import com.petland.modules.employee.controller.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.employee.specifications.EmployeeSpecification;
import com.petland.modules.employee.validator.EmployeeUpdateValidator;
import com.petland.common.auth.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;
    private final EmailValidator emailValidator;
    private final EmployeeUpdateValidator updateValidator;

    @Transactional
    public Employee register(EmployeeRequestDTO employeeRequestDTO){
        emailValidator.checkIfEmailExists(employeeRequestDTO.email());
        String encryptedPassword = passwordEncoder.encode(employeeRequestDTO.password());

        Employee employee = employeeMapper.toEntity(employeeRequestDTO);
        employee.setRole(Roles.ADMIN);
        employee.setPassword(encryptedPassword);
        return employeeRepository.save(employee);
    }

    public Employee findById(UUID employeeId){
       return employeeRepository.findById(employeeId)
                .filter(e -> !e.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Employee ID not found"));
    }

    public void deactivateById(UUID employeeId) {
       Employee employee = findById(employeeId);
       employee.setStatus(StatusEntity.DELETED);
       employeeRepository.save(employee);
    }

    public Page<EmployeeResponseDTO> listAllByFilter(EmployeeFilter filter, Pageable pageable){
         return employeeRepository.findAll(EmployeeSpecification.Specification(filter), pageable)
                 .map(employeeMapper::toDTO);
    }

    public EmployeeResponseDTO updateById(UUID employeeId, EmployeeUpdateDTO dto){
        Employee employee = findById(employeeId);
        employee = updateValidator.validator(employee, dto);
        employeeRepository.save(employee);
        return employeeMapper.toDTO(employee);
    }

}
