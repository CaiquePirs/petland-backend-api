package com.petland.employee;

import com.petland.common.auth.validator.EmailValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.EmailFoundException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.employee.builder.EmployeeFilter;
import com.petland.modules.employee.dto.EmployeeRequestDTO;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.employee.validator.EmployeeUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeMapper employeeMapper;
    @Mock private EmployeeUpdateValidator updateValidator;
    @Mock private EmailValidator emailValidator;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private EmployeeService employeeService;

    private Employee employee;
    private EmployeeRequestDTO employeeDTO;
    private String encryptedPassword;

    @BeforeEach
    void setUp(){
        encryptedPassword = "$2a$12$ZIAZzdPEm23ctqneXRpXIusicQ5PeQKl8W72j/w5EUKcvahJLE3Y.";
        employee = Employee.builder().password(encryptedPassword).email("employee@gmail.com").build();
        employeeDTO = EmployeeRequestDTO.builder().password("employee@1234").email("employee@gmail.com").build();
    }

    @Test
    void shouldRegisterEmployeeSuccessfully(){
        doNothing().when(emailValidator).checkIfEmailExists(employeeDTO.email());
        when(passwordEncoder.encode(employeeDTO.password())).thenReturn(encryptedPassword);
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);

        assertDoesNotThrow(() -> employeeService.register(employeeDTO));

        verify(employeeMapper).toEntity(employeeDTO);
        verify(passwordEncoder).encode(employeeDTO.password());
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmailExist(){
        doThrow(new EmailFoundException("This email already exists"))
                .when(emailValidator).checkIfEmailExists(employeeDTO.email());

        EmailFoundException ex = assertThrows(
                EmailFoundException.class,
                () -> employeeService.register(employeeDTO)
        );
        assertEquals("This email already exists", ex.getMessage());

        verify(emailValidator).checkIfEmailExists(employeeDTO.email());
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    @DisplayName("Should return employee when found and status is ACTIVE")
    void shouldReturnEmployeeWhenFoundAndActive() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setStatus(StatusEntity.ACTIVE);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findById(employeeId);

        assertEquals(employee, result);
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when employee is DELETED")
    void shouldThrowExceptionWhenEmployeeIsDeleted() {
        UUID employeeId = UUID.randomUUID();
        Employee deletedEmployee = new Employee();
        deletedEmployee.setStatus(StatusEntity.DELETED);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(deletedEmployee));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findById(employeeId));
        assertEquals("Employee ID not found", exception.getMessage());

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when employee is not found")
    void shouldThrowExceptionWhenEmployeeNotFound() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findById(employeeId));
        assertEquals("Employee ID not found", exception.getMessage());

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    @DisplayName("Should deactivate employee when found and active")
    void shouldDeactivateEmployeeSuccessfully() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setStatus(StatusEntity.ACTIVE);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        employeeService.deactivateById(employeeId);

        assertEquals(StatusEntity.DELETED, employee.getStatus());
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deactivating non-existent employee")
    void shouldThrowExceptionWhenDeactivatingNonExistentEmployee() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.deactivateById(employeeId));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update employee successfully")
    void shouldUpdateEmployeeSuccessfully() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = Employee.builder().build();
        Employee updatedEmployee = Employee.builder().build();
        EmployeeUpdateDTO dto = EmployeeUpdateDTO.builder().build();
        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder().build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(updateValidator.validator(employee, dto)).thenReturn(updatedEmployee);
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDTO(updatedEmployee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.updateById(employeeId, dto);

        assertEquals(responseDTO, result);
        verify(employeeRepository).save(updatedEmployee);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating deleted employee")
    void shouldThrowExceptionWhenUpdatingDeletedEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee deletedEmployee = new Employee();
        deletedEmployee.setStatus(StatusEntity.DELETED);
        EmployeeUpdateDTO dto = EmployeeUpdateDTO.builder().build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(deletedEmployee));

        assertThrows(NotFoundException.class, () -> employeeService.updateById(employeeId, dto));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should list employees by filter")
    void shouldListEmployeesByFilter() {
        EmployeeFilter filter = EmployeeFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee();
        EmployeeResponseDTO dto = EmployeeResponseDTO.builder().build();
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(employeePage);
        when(employeeMapper.toDTO(employee)).thenReturn(dto);

        Page<EmployeeResponseDTO> result = employeeService.listAllByFilter(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
        verify(employeeRepository).findAll(any(Specification.class), eq(pageable));
    }



}

