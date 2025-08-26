package com.petland.employee;


import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.common.entity.enums.Roles;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.employee.usecases.AuthEmployeeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthEmployeeUseCaseTest {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmployeeRepository repository;
    @InjectMocks
    private AuthEmployeeUseCase authUseCase;

    private String email;
    private Employee employee;
    private AuthRequestDTO authRequest;
    private StatusEntity status;

    @BeforeEach
    void setUp(){
        String passwordDecoded = "test1234";
        String passwordEncode ="$2a$12$3fFAzRPCT30WJMU49A9UoOG.iKQV.1/VUUsaYlE77uhAXd3qu/f0S";
        String SECRET_KEY = "SECRET";
        status = StatusEntity.ACTIVE;
        email = "employee@gmail.com";

        authRequest = new AuthRequestDTO(email, passwordDecoded);
        employee = Employee.builder().id(UUID.randomUUID()).role(Roles.CUSTOMER).email(email).password(passwordEncode).build();
        authUseCase = new AuthEmployeeUseCase(repository, passwordEncoder, SECRET_KEY);
    }


    @Test
    void shouldAuthenticateEmployeeSuccessfully(){
        when(repository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches(authRequest.password(), employee.getPassword())).thenReturn(true);

        AuthResponseDTO dto = authUseCase.execute(authRequest);

        assertNotNull(dto);
        assertNotNull(dto.getAccess_token());
        assertNotNull(dto.getExpire_in());

        verify(repository).findByEmailAndStatus(email, status);
        verify(passwordEncoder).matches(authRequest.password(), employee.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        when(repository.findByEmailAndStatus(email, status))
                .thenThrow(new InvalidCredentialsException("Email/password incorrect"));

        InvalidCredentialsException ex = assertThrows(
                InvalidCredentialsException.class,
                () -> authUseCase.execute(authRequest)
        );
        assertEquals("Email/password incorrect", ex.getMessage());
        verify(repository).findByEmailAndStatus(email, status);
    }

    @Test
    void shouldThrowExceptionWhenCustomerPasswordNotMatch(){
        employee.setPassword("password");
        when(repository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches(authRequest.password(), employee.getPassword())).thenReturn(false);

        InvalidCredentialsException ex = assertThrows(
                InvalidCredentialsException.class,
                () -> authUseCase.execute(authRequest)
        );

        assertEquals("Email/password incorrect", ex.getMessage());

        verify(repository).findByEmailAndStatus(email, status);
        verify(passwordEncoder).matches(authRequest.password(), employee.getPassword());
    }
}
