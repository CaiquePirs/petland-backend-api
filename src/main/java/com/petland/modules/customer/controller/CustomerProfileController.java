package com.petland.modules.customer.controller;

import com.petland.common.auth.AccessValidator;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.service.SaleService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final SaleService saleService;
    private final PetCareService petCareService;

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

    @GetMapping("/sales/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<SaleResponseDTO>> getMySalesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<SaleResponseDTO> salesList = saleService.findSalesByCustomerId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );

        return ResponseEntity.ok().body(salesList);
    }

    @GetMapping("/pet-care/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<PetCareHistoryResponseDTO>> getMyServicesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<PetCareHistoryResponseDTO> servicesHistoryList = petCareService.findAllServicesByCustomerId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );

        return ResponseEntity.ok().body(servicesHistoryList);
    }
}
