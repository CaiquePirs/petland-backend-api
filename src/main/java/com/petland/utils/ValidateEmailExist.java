package com.petland.utils;

import com.petland.exceptions.InvalidCredentialsException;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ValidateEmailExist {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public void validate(String email){
        boolean existCustomer = customerRepository
                .findByEmail(email).isPresent();

        boolean existEmployee = employeeRepository
                .findByEmail(email).isPresent();

        if(existCustomer || existEmployee){
            throw new InvalidCredentialsException("This email already exist");
        }

    }
}
