package com.petland.utils;

import com.petland.common.exception.EmailFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailValidator {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public void checkIfEmailExists(String email){
        boolean existCustomer = customerRepository
                .findByEmailAndStatus(email, StatusEntity.ACTIVE).isPresent();

        boolean existEmployee = employeeRepository
                .findByEmailAndStatus(email, StatusEntity.ACTIVE).isPresent();

        if(existCustomer || existEmployee){
            throw new EmailFoundException("This email already exists");
        }

    }
}
