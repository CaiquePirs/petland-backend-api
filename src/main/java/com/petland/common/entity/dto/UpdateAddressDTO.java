package com.petland.common.entity.dto;

public record UpdateAddressDTO(
         String street,
         String number,
         String zipCode,
         String state,
         String country,
         String city){}
