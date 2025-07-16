package com.petland.modules.customer.service;

import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.exceptions.EmailFoundException;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder encoder;

    public Customer register(CustomerRequestDTO customerRequestDTO){
        repository.findByEmail(customerRequestDTO.email())
                .ifPresent(customer -> {
                  throw new EmailFoundException("This email already exist");
                });

        var encryptedPassword = encoder.encode(customerRequestDTO.password());

        var customer = customerMapper.toEntity(customerRequestDTO);
        customer.setRole(Roles.CUSTOMER);
        customer.setStatus(StatusEntity.ACTIVE);
        customer.setPassword(encryptedPassword);

        return repository.save(customer);
    }
}
