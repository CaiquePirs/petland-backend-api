package com.petland.modules.customer.service;

import com.petland.common.entity.Address;
import com.petland.modules.customer.dto.UpdateCustomerDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.utils.EmailValidator;
import com.petland.utils.AddressUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUpdateValidator {

    private final EmailValidator emailValidator;
    private final AddressUpdateValidator validateAddress;
    private final PasswordEncoder passwordEncoder;

    public Customer validate(Customer customer, UpdateCustomerDTO updateCustomer){
        if(updateCustomer.email() != null){
            emailValidator.checkIfEmailExists(updateCustomer.email());
            customer.setEmail(updateCustomer.email());
        }

        if(updateCustomer.password() != null){
            customer.setPassword(passwordEncoder.encode(updateCustomer.password()));
        }

        if(updateCustomer.phone() != null){
            customer.setPhone(updateCustomer.phone());
        }

        if(updateCustomer.name() != null){
            customer.setName(updateCustomer.name());
        }

        if(updateCustomer.dateBirth() != null){
            customer.setDateBirth(updateCustomer.dateBirth());
        }

        Address addressUpdated = validateAddress.validate(
                updateCustomer.addressDTO(),
                customer.getAddress()
        );

        customer.setAddress(addressUpdated);
        return customer;
    }
}
