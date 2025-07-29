package com.petland.modules.employee.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.dto.EmployeeRequestDTO;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.employee.specifications.EmployeeSpecification;
import com.petland.utils.EmailValidator;
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
                .orElseThrow(() -> new NotFoundException("Employer not found"));
    }

    public void deleteById(UUID employeeId) {
       Employee employee = findById(employeeId);
       employee.setStatus(StatusEntity.DELETED);
       employeeRepository.save(employee);
    }

    public Page<EmployeeResponseDTO> listAllEmployee(String name, String email, String phone, String department, String status, Pageable pageable){
         return employeeRepository
                 .findAll(EmployeeSpecification.Specification(name, email, phone, department, status), pageable)
                 .map(employeeMapper::toDTO);
    }

    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeUpdateDTO dto){
        Employee employee = findById(employeeId);
        employee = updateValidator.validator(employee, dto);
        employeeRepository.save(employee);
        return employeeMapper.toDTO(employee);
    }

}
