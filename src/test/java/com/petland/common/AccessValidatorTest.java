package com.petland.common;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class AccessValidatorTest {

    @Mock HttpServletRequest request;
    @Mock EmployeeService employeeService;
    @InjectMocks AccessValidator accessValidator;

    private String userIdLogged;
    private UUID userId;
    private UUID employeeId;
    private Employee employee;

    @BeforeEach
    void setUp(){
        userIdLogged = "200b0bbb-6ef3-4de6-bc7b-f78d2dd70331";
        employeeId = UUID.fromString("200b0bbb-6ef3-4de6-bc7b-f78d2dd70331");
        employee = new Employee();
        userId = UUID.randomUUID();
    }

    @Test
    void shouldReturnIdLoggedUser(){
        when(request.getAttribute("id")).thenReturn(userIdLogged);
        assertNotNull(accessValidator.getLoggedInUser());

        verify(request).getAttribute("id");
    }

    @Test
    void shouldReturnNullIfDontHaveUserLogged(){
        when(request.getAttribute("id")).thenReturn(null);
        assertNull(accessValidator.getLoggedInUser());

        verify(request).getAttribute("id");
    }

    @Test
    void shouldReturnAnEmployeeLogged(){
        when(accessValidator.getLoggedInUser()).thenReturn(employeeId);
        when(employeeService.findById(employeeId)).thenReturn(employee);

        assertDoesNotThrow(() -> accessValidator.getEmployeeLogged());
        verify(employeeService).findById(employeeId);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        when(request.getAttribute("id")).thenReturn(null);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> accessValidator.getEmployeeLogged()
        );
        assertEquals("User not authorized", ex.getMessage());

        verify(request).getAttribute("id");
        verifyNoInteractions(employeeService);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound(){
        when(request.getAttribute("id")).thenReturn(employeeId.toString());
        when(employeeService.findById(employeeId)).thenThrow(new NotFoundException("Employer not found"));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> accessValidator.getEmployeeLogged()
        );
        assertEquals("Employer not found", ex.getMessage());

        verify(request, times(2)).getAttribute("id");
        verify(employeeService).findById(employeeId);
    }

    @Test
    void shouldReturnTrueIfAdmin(){
        when(request.isUserInRole("ADMIN")).thenReturn(true);
        when(request.getAttribute("id")).thenReturn(false);

        assertDoesNotThrow(() -> accessValidator.isOwnerOrAdmin(userId));

        verify(request).isUserInRole("ADMIN");
        verify(request).getAttribute("id");
    }

    @Test
    void shouldReturnTrueIfIUser(){
        when(request.isUserInRole("ADMIN")).thenReturn(false);
        when(request.getAttribute("id")).thenReturn(userId);

        assertDoesNotThrow(() -> accessValidator.isOwnerOrAdmin(userId));

        verify(request).isUserInRole("ADMIN");
        verify(request).getAttribute("id");
    }

    @Test
    void shouldThrowExceptionWhenIsNotAdminOrUser(){
        when(request.isUserInRole("ADMIN")).thenReturn(false);
        when(request.getAttribute("id")).thenReturn(UUID.randomUUID());

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> accessValidator.isOwnerOrAdmin(userId)
        );
        assertEquals("User not authorized", ex.getMessage());

        verify(request).isUserInRole("ADMIN");
        verify(request).getAttribute("id");
    }
}
