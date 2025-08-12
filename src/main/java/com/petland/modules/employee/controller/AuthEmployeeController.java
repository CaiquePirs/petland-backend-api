package com.petland.modules.employee.controller;

import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.modules.employee.dto.EmployeeRequestDTO;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.usecases.AuthEmployeeUseCase;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/employees")
@RequiredArgsConstructor
public class AuthEmployeeController {

    private final AuthEmployeeUseCase authEmployeeUseCase;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO authRequestDTO){
       AuthResponseDTO authResponseDTO = authEmployeeUseCase.execute(authRequestDTO);
       return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> register(@RequestBody @Valid EmployeeRequestDTO employeeRequestDTO){
        Employee employee = employeeService.register(employeeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeMapper.toDTO(employee));
    }
}
