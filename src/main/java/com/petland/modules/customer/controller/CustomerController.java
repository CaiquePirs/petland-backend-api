package com.petland.modules.customer.controller;

import com.petland.common.auth.AccessValidator;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final AccessValidator accessValidator;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable(name = "id") UUID customerId){
        Customer customer = customerService.findCustomerById(customerId);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable(name = "id") UUID customerId){
        accessValidator.isOwnerOrAdmin(customerId);
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CustomerResponseDTO>> listAllCustomers(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "status", required = false) StatusEntity status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Page<CustomerResponseDTO> customersResponseDTO = customerService.listAllCustomerByFilter(
                name, email, phone, status, PageRequest.of(page, size)
        );
        return ResponseEntity.ok().body(customersResponseDTO);
    }









}
