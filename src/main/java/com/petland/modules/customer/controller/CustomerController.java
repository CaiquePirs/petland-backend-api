package com.petland.modules.customer.controller;

import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final HttpServletRequest request;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> findProfile(){
        String customerId = request.getAttribute("id").toString();
        Customer customer = customerService.findCustomerById(UUID.fromString(customerId));
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable(name = "id") UUID customerId){
        Customer customer = customerService.findCustomerById(customerId);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }
}
