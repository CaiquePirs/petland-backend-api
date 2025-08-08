package com.petland.modules.employee.validator;

import com.petland.common.entity.Address;
import com.petland.modules.employee.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.model.Employee;
import com.petland.common.entity.AddressUpdateValidator;
import com.petland.common.auth.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeUpdateValidator {

    private final AddressUpdateValidator addressValidator;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    public Employee validator(Employee employee, EmployeeUpdateDTO dto){
        if(dto.addressDTO() != null){
            Address addressUpdated = addressValidator.validate(dto.addressDTO(), employee.getAddress());
            employee.setAddress(addressUpdated);
        }

        if(dto.name() != null && !dto.name().isBlank()){
            employee.setName(dto.name());
        }

        if(dto.email() != null && !dto.email().isBlank()){
            emailValidator.checkIfEmailExists(dto.email());
            employee.setEmail(dto.email());
        }

        if(dto.password() != null && !dto.password().isBlank()){
            employee.setPassword(passwordEncoder.encode(dto.password()));
        }

        if(dto.department() != null && !dto.department().toString().isBlank()){
            employee.setDepartment(dto.department());
        }

        if(dto.phone() != null && !dto.phone().isBlank()){
            employee.setPhone(dto.phone());
        }

        if(dto.dateBirth() != null){
            employee.setDateBirth(dto.dateBirth());
        }

        if(dto.hireDate() != null){
            employee.setHireDate(dto.hireDate());
        }

        if(dto.role() != null && !dto.role().toString().isBlank()){
            employee.setRole(dto.role());
        }
        return employee;
    }
}
