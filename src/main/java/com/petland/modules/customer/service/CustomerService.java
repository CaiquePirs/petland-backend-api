package com.petland.modules.customer.service;

import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.utils.ValidateEmailExist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ValidateEmailExist validateEmail;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public Customer register(CustomerRequestDTO customerRequestDTO){
        validateEmail.validate(customerRequestDTO.email());
        var encryptedPassword = passwordEncoder.encode(customerRequestDTO.password());

        var customer = customerMapper.toEntity(customerRequestDTO);
        customer.setRole(Roles.CUSTOMER);
        customer.setStatus(StatusEntity.ACTIVE);
        customer.setPassword(encryptedPassword);

        return customerRepository.save(customer);
    }
}
