package com.petland.modules.customer.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.utils.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailValidator validateEmail;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public Customer register(CustomerRequestDTO customerRequestDTO){
        validateEmail.checkIfEmailExists(customerRequestDTO.email());
        String encryptedPassword = passwordEncoder.encode(customerRequestDTO.password());

        Customer customer = customerMapper.toEntity(customerRequestDTO);
        customer.setRole(Roles.CUSTOMER);
        customer.setStatus(StatusEntity.ACTIVE);
        customer.setPassword(encryptedPassword);

        return customerRepository.save(customer);
    }

    public Customer findCustomerById(UUID customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(customer.getStatus().equals(StatusEntity.DELETED)){
            throw new NotFoundException("User not found");
        }

        return customer;
    }

}
