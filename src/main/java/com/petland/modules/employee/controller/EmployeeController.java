package com.petland.modules.employee.controller;

import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID customerId){
        employeeService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EmployeeResponseDTO>> findAllEmployeeByFilter(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                             @RequestParam(value = "size", defaultValue = "10") int size){
        Page<EmployeeResponseDTO> employeeList = employeeService.listAllEmployee(PageRequest.of(page, size));
        return ResponseEntity.ok().body(employeeList);
    }

}
