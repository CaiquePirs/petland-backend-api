package com.petland.modules.employee.controller;

import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.modules.employee.service.AuthEmployeeUseCase;
import com.petland.modules.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/employees")
@RequiredArgsConstructor
public class AuthEmployeeController {

    private final AuthEmployeeUseCase authEmployeeUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody @Valid AuthRequestDTO authRequestDTO){
       AuthResponseDTO authResponseDTO = authEmployeeUseCase.execute(authRequestDTO);
       return ResponseEntity.ok(authResponseDTO);
    }

}
