package com.petland.employee;

import com.petland.common.entity.Address;
import com.petland.modules.employee.controller.dto.EmployeeRequestDTO;
import com.petland.modules.employee.model.enums.Department;
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
import org.junit.jupiter.api.DisplayName;

public class EmployeeRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Arguments> generateInvalidArguments() {
        Address address = new Address();
        Department department = Department.MANAGER;
        LocalDate hireDate = LocalDate.now();

        return Stream.of(
                Arguments.of(null, "test@gmail.com", "password1234", "90 0909-09992", department, address, hireDate, LocalDate.of(2004, 4, 4), "Name is required"),
                Arguments.of("Test", null, "password1234", "90 0909-09992", department, address, hireDate, LocalDate.of(2004, 4, 4), "Email is required"),
                Arguments.of("Test", "test.com", "password1234", "90 0909-09992", department, address, hireDate, LocalDate.of(2004, 4, 4), "Email must be valid"),
                Arguments.of("Test", "test@gmail.com", null, "90 0909-09992", department, address, hireDate, LocalDate.of(2004, 4, 4), "Password is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", null, department, address, hireDate, LocalDate.of(2004, 4, 4), "Phone is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", null, address, hireDate, LocalDate.of(2004, 4, 4), "Department is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", department, null, hireDate, LocalDate.of(2004, 4, 4), "Address is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", department, address, null, LocalDate.of(2004, 4, 4), "Hire date is required"),
                Arguments.of("Test", "test@gmail.com", "password1234", "90 0909-09992", department, address, hireDate, null, "Date birth is required")
        );
    }

    @ParameterizedTest(name = "Test - {index} - Expected = {8}")
    @MethodSource("generateInvalidArguments")
    void shouldValidateInvalidArguments(String name, String email, String password,
                                        String phone, Department department, Address address,
                                        LocalDate hireDate, LocalDate dateBirth, String expectedMessage) {

        EmployeeRequestDTO request = new EmployeeRequestDTO(name, email, password, phone, department, address, hireDate, dateBirth);
        Set<ConstraintViolation<EmployeeRequestDTO>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    @DisplayName("Should accept valid EmployeeRequestDTO")
    void shouldAcceptRequestWithValidValues() {
        Address address = new Address();
        Department department = Department.MANAGER;
        LocalDate hireDate = LocalDate.now();
        LocalDate dateBirth = LocalDate.of(2004, 4, 4);

        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "Test", "test@gmail.com", "test1234", "083 - 0909-1423",
                department, address, hireDate, dateBirth
        );

        Set<ConstraintViolation<EmployeeRequestDTO>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());

        assertAll(
                () -> assertEquals("Test", request.name()),
                () -> assertEquals("test@gmail.com", request.email()),
                () -> assertEquals("test1234", request.password()),
                () -> assertEquals("083 - 0909-1423", request.phone()),
                () -> assertEquals(department, request.department()),
                () -> assertEquals(address, request.address()),
                () -> assertEquals(hireDate, request.hireDate()),
                () -> assertEquals(dateBirth, request.dateBirth())
        );
    }
}
