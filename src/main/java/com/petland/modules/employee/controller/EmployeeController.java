package com.petland.modules.employee.controller;

import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final HttpServletRequest request;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> getProfile(){
        String employerId = request.getAttribute("id").toString();
        Employee employee = employeeService.findById(UUID.fromString(employerId));
        return ResponseEntity.ok(employeeMapper.toDTO(employee));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable(name = "id") UUID employeeId){
        Employee employee = employeeService.findById(employeeId);
        return ResponseEntity.ok().body(employeeMapper.toDTO(employee));
    }
}
