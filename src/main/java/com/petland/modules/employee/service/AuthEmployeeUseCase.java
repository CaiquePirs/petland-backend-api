package com.petland.modules.employee.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.common.exception.InvalidCredentialsException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder encoder;
    private final String SECRET_KEY;

    public AuthEmployeeUseCase(EmployeeRepository employeeRepository, PasswordEncoder encoder,
                               @Value("${SECRET_KEY_EMPLOYEE}") String SECRET_KEY) {
        this.employeeRepository = employeeRepository;
        this.encoder = encoder;
        this.SECRET_KEY = SECRET_KEY;
    }

    public AuthResponseDTO execute(AuthRequestDTO authRequestDTO) {
        Employee employee = employeeRepository.findByEmailAndStatus(authRequestDTO.email(), StatusEntity.ACTIVE)
                .orElseThrow(() -> new InvalidCredentialsException("Email/password incorrect"));

        boolean matches = encoder.matches(authRequestDTO.password(), employee.getPassword());

        if(!matches){
            throw new InvalidCredentialsException("Email/password incorrect");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Instant expiresIn = Instant.now().plus(Duration.ofHours(10));

        var token = JWT.create()
                .withSubject(employee.getId().toString())
                .withClaim("roles", List.of(Roles.ADMIN.toString()))
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return AuthResponseDTO.builder()
                .access_token(token)
                .expire_in(expiresIn.toEpochMilli())
                .build();

    }
}
