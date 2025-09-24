package com.petland.common.utils;

import com.petland.common.entity.Address;
import com.petland.common.entity.dto.UpdateAddressDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressUpdateValidator {

    public Address validate(UpdateAddressDTO addressDTO, Address address){
        if(addressDTO.city() != null){
            address.setCity(addressDTO.city());
        }

        if(addressDTO.country() != null){
            address.setCountry(addressDTO.country());
        }

        if(addressDTO.number() != null){
            address.setNumber(addressDTO.number());
        }

        if(addressDTO.zipCode() != null){
            address.setZipCode(addressDTO.zipCode());
        }

        if(addressDTO.street() != null){
            address.setStreet(addressDTO.street());
        }

        if(addressDTO.state() != null){
            address.setState(addressDTO.state());
        }
        return address;
    }
}
