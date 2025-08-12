package com.petland.modules.employee.controller;

import com.petland.modules.employee.builder.EmployeeFilter;
import com.petland.modules.employee.dto.EmployeeResponseDTO;
import com.petland.modules.employee.dto.EmployeeUpdateDTO;
import com.petland.modules.employee.mappers.EmployeeMapper;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<EmployeeResponseDTO> findEmployeeById(@PathVariable(name = "id") UUID employeeId){
        Employee employee = employeeService.findById(employeeId);
        return ResponseEntity.ok(employeeMapper.toDTO(employee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateEmployeeById(@PathVariable(name = "id") UUID customerId){
        employeeService.deactivateById(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EmployeeResponseDTO>> findAllEmployeeByFilter(
            @ModelAttribute EmployeeFilter filter,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<EmployeeResponseDTO> employeeList = employeeService.listAllByFilter(filter, PageRequest.of(page, size));
        return ResponseEntity.ok(employeeList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> updateEmployeeById(@PathVariable(name = "id") UUID employeeId,
                                                                  @RequestBody @Valid EmployeeUpdateDTO employeeDTO) {
        EmployeeResponseDTO employeeResponse = employeeService.updateById(employeeId, employeeDTO);
        return ResponseEntity.ok(employeeResponse);
    }

}
