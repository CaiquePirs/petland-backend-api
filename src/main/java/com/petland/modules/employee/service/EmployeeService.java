package com.petland.modules.employee.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.dto.EmployeeRequestDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.utils.EmailValidator;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Employee register(EmployeeRequestDTO employeeRequestDTO){
        emailValidator.checkIfEmailExists(employeeRequestDTO.email());

        String encryptedPassword = passwordEncoder.encode(employeeRequestDTO.password());

        Employee employee = employeeMapper.toEntity(employeeRequestDTO);
        employee.setStatus(StatusEntity.ACTIVE);
        employee.setRole(Roles.ADMIN);
        employee.setPassword(encryptedPassword);

        return employeeRepository.save(employee);
    }

    public Employee findById(UUID employeeId){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employer not found"));

        if(employee.getStatus().equals(StatusEntity.DELETED)){
            throw new NotFoundException("Employer not found");
        }

        return employee;
    }

    public void deleteById(UUID employeeId) {
       Employee employee = findById(employeeId);
       employee.setStatus(StatusEntity.DELETED);
       employeeRepository.save(employee);
    }

}
