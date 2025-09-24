package com.petland.modules.customer.controller;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.modules.customer.controller.doc.CustomerApi;
import com.petland.modules.customer.builder.CustomerFilter;
import com.petland.modules.customer.controller.dto.CustomerResponseDTO;
import com.petland.modules.customer.controller.dto.UpdateCustomerDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final CustomerService service;
    private final CustomerMapper customerMapper;
    private final AccessValidator accessValidator;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable(name = "id") UUID customerId){
        Customer customer = service.findById(customerId);
        return ResponseEntity.ok(customerMapper.toDTO(customer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Void> deactivateCustomerById(@PathVariable(name = "id") UUID customerId){
        accessValidator.isOwnerOrAdmin(customerId);
        service.deactivateById(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CustomerResponseDTO>> listAllCustomersByFilter(
            @ModelAttribute CustomerFilter filter,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        return ResponseEntity.ok(service.listAllByFilter(filter, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> updateCustomerById(
            @PathVariable(name = "id") UUID customerId,
            @RequestBody @Valid UpdateCustomerDTO updateCustomerDTO){

        accessValidator.isOwnerOrAdmin(customerId);
        Customer customer = service.updateById(customerId, updateCustomerDTO);
        return ResponseEntity.ok(customerMapper.toDTO(customer));
    }
}
