package com.petland.modules.customer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.AuthCustomerRequestDTO;
import com.petland.modules.customer.dto.AuthCustomerResponseDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;
    private final String SECRET_KEY;

    public AuthCustomerUseCase(CustomerRepository customerRepository, PasswordEncoder encoder,
                                @Value("${SECRET_KEY}") String SECRET_KEY) {
        this.customerRepository = customerRepository;
        this.encoder = encoder;
        this.SECRET_KEY = SECRET_KEY;
    }

    public AuthCustomerResponseDTO execute(AuthCustomerRequestDTO authCustomerRequestDTO) {
        Customer customer = customerRepository.findByEmailAndStatus(authCustomerRequestDTO.email(), StatusEntity.ACTIVE)
                .orElseThrow(() -> new InvalidCredentialsException("Email/password incorrect"));

        boolean matches = encoder.matches(authCustomerRequestDTO.password(), customer.getPassword());

        if(!matches){
            throw new InvalidCredentialsException("Email/password incorrect");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Instant expiresIn = Instant.now().plus(Duration.ofMinutes(10));

        var token = JWT.create()
                .withSubject(customer.getId().toString())
                .withClaim("roles", List.of(Roles.CUSTOMER.toString()))
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return AuthCustomerResponseDTO.builder()
                .access_token(token)
                .expire_in(expiresIn.toEpochMilli())
                .build();

    }
}
