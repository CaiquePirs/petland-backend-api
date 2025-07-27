package com.petland.modules.customer.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.customer.specifications.CustomerSpecification;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.utils.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailValidator validateEmail;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final PetRepository petRepository;
    private final CustomerSpecification specification;

    public Customer register(CustomerRequestDTO customerRequestDTO){
        validateEmail.checkIfEmailExists(customerRequestDTO.email());
        String encryptedPassword = passwordEncoder.encode(customerRequestDTO.password());

        Customer customer = customerMapper.toEntity(customerRequestDTO);
        customer.setRole(Roles.CUSTOMER);
        customer.setPassword(encryptedPassword);
        return customerRepository.save(customer);
    }

    public Customer findCustomerById(UUID customerId){
        return customerRepository.findById(customerId)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public void deleteById(UUID customerId) {
        Customer customer = findCustomerById(customerId);
        customer.setStatus(StatusEntity.DELETED);

        List<Pet> petByCustomer = Optional.ofNullable(customer.getMyPets())
                .orElse(Collections.emptyList());

        if (!petByCustomer.isEmpty()) {
            for (Pet pet : petByCustomer) {
                pet.setStatus(StatusEntity.DELETED);
            }
            petRepository.saveAll(petByCustomer);
        }
        customerRepository.save(customer);
    }

    public Page<CustomerResponseDTO> listAllCustomerByFilter(String name, String email, String phone, StatusEntity status, Pageable pageable){
     Specification<Customer>  filterCustomers = CustomerSpecification.filterBy(name, email, phone, status);
     Page<Customer> customers = customerRepository.findAll(filterCustomers, pageable);
     return customers.map(customerMapper::toDTO);
    }

}
