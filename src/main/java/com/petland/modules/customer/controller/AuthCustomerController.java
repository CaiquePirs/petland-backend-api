package com.petland.modules.customer.controller;

import com.petland.common.auth.dto.AuthRequestDTO;
import com.petland.common.auth.dto.AuthResponseDTO;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.usecases.AuthCustomerUseCase;
import com.petland.modules.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/customers")
@RequiredArgsConstructor
public class AuthCustomerController {

    private final AuthCustomerUseCase authCustomerUseCase;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO){
        AuthResponseDTO authResponse = authCustomerUseCase.execute(authRequestDTO);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(@RequestBody @Valid CustomerRequestDTO customerRequestDTO){
        Customer customer = customerService.register(customerRequestDTO);
        CustomerResponseDTO customerResponseDTO = customerMapper.toDTO(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponseDTO);
    }
}
