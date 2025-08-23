package com.petland.common;

import com.petland.common.auth.dto.AuthRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class AuthRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Arguments> providenceInvalidArguments(){
        return Stream.of(
                Arguments.of("test@gmail.com", null, List.of("Password is required")),
                Arguments.of(null, "test123456", List.of("Email is required")),
                Arguments.of("test.com", "test123456", List.of("Email must be valid")),
                Arguments.of(null, null, List.of("Email is required", "Password is required"))
        );
    }

    @ParameterizedTest(name = "Invalid field test #{index} - expected: {3}")
    @MethodSource("providenceInvalidArguments")
    void shouldValidateInvalidArguments(String email, String password, List<String> message){
        AuthRequestDTO dto = new AuthRequestDTO(email, password);
        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.stream().anyMatch(auth -> message.contains(auth.getMessage())),
                "Expected message: " + message);
    }

    @Test
    void shouldAcceptValidFields(){
        String email = "test@gmail.com";
        String password = "test123456";
        AuthRequestDTO dto = new AuthRequestDTO(email, password);

        assertDoesNotThrow(() -> validator.validate(dto));
        assertAll(
                () -> assertEquals(email, dto.email()),
                () -> assertEquals(password, dto.password())
        );
    }
}
