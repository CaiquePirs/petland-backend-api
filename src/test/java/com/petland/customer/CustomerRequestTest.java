package com.petland.customer;

import com.petland.common.entity.Address;
import com.petland.modules.customer.controller.dto.CustomerRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Arguments> generateInvalidArguments(){
        Address address = new Address();
        return Stream.of(
                Arguments.of(null, "test@gmail.com", "password1234", "90 0909-09992", LocalDate.of(2004, 04, 04), address, "Name is required"),
                Arguments.of("Test", null, "password1234", "90 0909-09992", LocalDate.of(2004, 04, 04), address, "Email is required"),
                Arguments.of("Test", "test.com", "password1234", "90 0909-09992", LocalDate.of(2004, 04, 04), address, "Email must be valid"),
                Arguments.of("Test", "test@gmail.com", null, "90 0909-09992", LocalDate.of(2004, 04, 04), address, "Password is required"),
                Arguments.of("Test", "test@gmail.com", "1234", "90 0909-09992", LocalDate.of(2004, 04, 04), address, "Password must be up to 8 characters"),
                Arguments.of("Test", "test@gmail.com", "password1234", null, LocalDate.of(2004, 04, 04), address, "Phone is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", null, address, "DateBirth is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", LocalDate.now().plusDays(1), address, "The date cannot be in the future"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", LocalDate.now().plusDays(1), null, "Address is required")
        );
    }

    @ParameterizedTest(name = "Test - {index} - Expected = {6}")
    @MethodSource("generateInvalidArguments")
    void shouldValidateInvalidArguments(String name, String email, String password,
                                                   String phone, LocalDate dateBirth, Address address, String expectedMessage){

        CustomerRequestDTO request = new CustomerRequestDTO(name, email, password, phone, dateBirth, address);
        Set<ConstraintViolation<CustomerRequestDTO>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    void shouldAcceptRequestWithValidValues(){
        Address address = new Address();
        CustomerRequestDTO request = new CustomerRequestDTO(
                "Test", "test@gmail.com", "test1234", "083 - 0909-1423",
                LocalDate.of(2004, 04, 04), address
        );

        validator.validate(request);
        assertAll(
                () -> assertEquals("Test", request.name()),
                () -> assertEquals("test@gmail.com", request.email()),
                () -> assertEquals("test1234", request.password()),
                () -> assertEquals("083 - 0909-1423", request.phone()),
                () -> assertEquals(LocalDate.of(2004, 04, 04), request.dateBirth()),
                () -> assertEquals(address, request.address())
        );
    }
}
