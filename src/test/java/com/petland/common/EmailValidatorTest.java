package com.petland.common;

import com.petland.common.auth.validator.EmailValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.EmailFoundException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class EmailValidatorTest {

    @Mock CustomerRepository customerRepository;
    @Mock EmployeeRepository employeeRepository;
    @InjectMocks private EmailValidator validator;

    private String email;
    private StatusEntity status;
    private Employee employee;
    private Customer customer;

    @BeforeEach
    void setUp(){
        email = "test@gmail.com";
        status = StatusEntity.ACTIVE;
        employee = new Employee();
        customer = new Customer();
    }

    @Test
    void shouldAllowRegisteringNewEmail(){
       when(customerRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.empty());
       when(employeeRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.empty());

       assertDoesNotThrow(() -> validator.checkIfEmailExists(email));

       verify(customerRepository).findByEmailAndStatus(email, status);
       verify(employeeRepository).findByEmailAndStatus(email, status);
    }

    @Test
    void shouldNotAllowRegisterEmailIfExistEmployee(){
        when(customerRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(employee));

        EmailFoundException ex = assertThrows(EmailFoundException.class, () -> validator.checkIfEmailExists(email));
        assertEquals("This email already exists", ex.getMessage());

        verify(employeeRepository).findByEmailAndStatus(email, status);
        verify(customerRepository).findByEmailAndStatus(email, status);
    }

    @Test
    void shouldNotAllowRegisterEmailIfExistCustomer(){
        when(customerRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(customer));
        when(employeeRepository.findByEmailAndStatus(email, status)).thenReturn(Optional.empty());

        EmailFoundException ex = assertThrows(EmailFoundException.class, () -> validator.checkIfEmailExists(email));
        assertEquals("This email already exists", ex.getMessage());

        verify(employeeRepository).findByEmailAndStatus(email, status);
        verify(customerRepository).findByEmailAndStatus(email, status);
    }


}
