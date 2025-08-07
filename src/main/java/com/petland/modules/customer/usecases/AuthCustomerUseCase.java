package com.petland.modules.customer.usecases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.common.entity.enums.Roles;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.auth.dto.AuthResponseDTO;
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

    public AuthResponseDTO execute(AuthRequestDTO authRequestDTO) {
        Customer customer = customerRepository.findByEmailAndStatus(authRequestDTO.email(), StatusEntity.ACTIVE)
                .orElseThrow(() -> new InvalidCredentialsException("Email/password incorrect"));

        boolean matches = encoder.matches(authRequestDTO.password(), customer.getPassword());

        if(!matches){
            throw new InvalidCredentialsException("Email/password incorrect");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Instant expiresIn = Instant.now().plus(Duration.ofHours(10));

        var token = JWT.create()
                .withSubject(customer.getId().toString())
                .withClaim("roles", List.of(Roles.CUSTOMER.toString()))
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return AuthResponseDTO.builder()
                .access_token(token)
                .expire_in(expiresIn.toEpochMilli())
                .build();

    }
}
