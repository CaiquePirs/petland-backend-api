package com.petland.modules.customer.controller;

import com.petland.common.exception.UnauthorizedException;
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
    public ResponseEntity<CustomerResponseDTO> getProfile(){
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID customerId){
        var currentUserId = request.getAttribute("id").toString();
        boolean isAdmin = request.isUserInRole("ADMIN");

        if(!isAdmin && !UUID.fromString(currentUserId).equals(customerId)){
            throw new UnauthorizedException("User not authorized");
        }
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
