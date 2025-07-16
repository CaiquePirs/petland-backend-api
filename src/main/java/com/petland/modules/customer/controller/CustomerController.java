package com.petland.modules.customer.controller;

import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;
    private final CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> register(@RequestBody @Valid CustomerRequestDTO customerRequestDTO){
        var customer = service.register(customerRequestDTO);
        var customerResponseDTO = customerMapper.toDTO(customer);
        return ResponseEntity.ok().body(customerResponseDTO);
    }
}
