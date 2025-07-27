package com.petland.modules.customer.controller;

import com.petland.common.auth.AccessValidator;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/me")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final AccessValidator accessValidator;
    private final CustomerService customerService;
    private final PetService petService;
    private final PetMapper petMapper;
    private final CustomerMapper customerMapper;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getMyProfile(){
        Customer customer = customerService.findCustomerById(accessValidator.getLoggedInUser());
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/pets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PetResponseDTO>> getMyPets(){
        List<PetResponseDTO> myPets = petService.getPetsByCustomerId(accessValidator.getLoggedInUser())
                .stream()
                .map(petMapper::toDTO).toList();
        return ResponseEntity.ok().body(myPets);
    }
}
