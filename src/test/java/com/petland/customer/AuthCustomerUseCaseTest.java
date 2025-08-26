package com.petland.customer;

import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.common.entity.enums.Roles;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.customer.usecases.AuthCustomerUseCase;
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
public class AuthCustomerUseCaseTest {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CustomerRepository repository;
    @InjectMocks private AuthCustomerUseCase authUseCase;

    private String email;
    private Customer customer;
    private AuthRequestDTO authRequest;
    private StatusEntity status;

    @BeforeEach
    void setUp(){
        String passwordDecoded = "test1234";
        String passwordEncode ="$2a$12$3fFAzRPCT30WJMU49A9UoOG.iKQV.1/VUUsaYlE77uhAXd3qu/f0S";
        String SECRET_KEY = "SECRET";
        status = StatusEntity.ACTIVE;
        email = "customer@gmail.com";

        authRequest = new AuthRequestDTO(email, passwordDecoded);
        customer = Customer.builder().id(UUID.randomUUID()).role(Roles.CUSTOMER).email(email).password(passwordEncode).build();
        authUseCase = new AuthCustomerUseCase(repository, passwordEncoder, SECRET_KEY);
    }

    @Test
    void shouldAuthenticateCustomerSuccessfully(){
        when(repository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(authRequest.password(), customer.getPassword())).thenReturn(true);

        AuthResponseDTO dto = authUseCase.execute(authRequest);

        assertNotNull(dto);
        assertNotNull(dto.getAccess_token());
        assertNotNull(dto.getExpire_in());

        verify(repository).findByEmailAndStatus(email, status);
        verify(passwordEncoder).matches(authRequest.password(), customer.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound(){
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
        customer.setPassword("password");
        when(repository.findByEmailAndStatus(email, status)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(authRequest.password(), customer.getPassword())).thenReturn(false);

       InvalidCredentialsException ex = assertThrows(
                InvalidCredentialsException.class,
                () -> authUseCase.execute(authRequest)
        );

       assertEquals("Email/password incorrect", ex.getMessage());

       verify(repository).findByEmailAndStatus(email, status);
       verify(passwordEncoder).matches(authRequest.password(), customer.getPassword());
    }

}
