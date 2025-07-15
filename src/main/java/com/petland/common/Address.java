package com.petland.common;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String street;
    private String number;
    private String zipCode;
    private String state;
    private String contry;
    private String city;
}