package com.petland.modules.customer.controller;

import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<CustomerResponseDTO> findById(HttpServletRequest request){
        String customerId = request.getAttribute("customer_id").toString();
        Customer customer = customerService.findCustomerById(UUID.fromString(customerId));
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }
}
