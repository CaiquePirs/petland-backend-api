package com.petland.utils;

import com.petland.exceptions.EmailFoundException;
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
                .findByEmail(email).isPresent();

        boolean existEmployee = employeeRepository
                .findByEmail(email).isPresent();

        if(existCustomer || existEmployee){
            throw new EmailFoundException("This email already exists");
        }

    }
}
