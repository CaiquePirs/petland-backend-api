package com.petland.pet;


import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.validator.PetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PetValidatorTest {

    private PetValidator petValidator;
    private Customer customer;
    private Pet pet;

    @BeforeEach
    void setUp() {
        petValidator = new PetValidator();
        pet = new Pet();
        customer = new Customer();
    }

    @Test
    void shouldValidateThePetOwnerSuccessfully(){
        pet.setOwner(customer);
        customer.setMyPets(new ArrayList<>());
        customer.setMyPets(List.of(pet));

        assertDoesNotThrow(
                () -> petValidator.isPetOwner(pet, customer)
        );

        assertTrue(customer.getMyPets().contains(pet));
        assertNotNull(customer);
        assertNotNull(customer.getMyPets());
        assertNotNull(pet);
        assertNotNull(pet.getOwner());
    }

    @Test
    void shouldThrowExceptionWhenPetDoesNotBelongToCustomer(){
        Pet dog = new Pet();

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> petValidator.isPetOwner(dog, customer)
        );
        assertEquals("This pet does not belong to this customer", ex.getMessage());
        assertFalse(customer.getMyPets().contains(dog));
        assertFalse(customer.getMyPets().contains(dog));
    }

    @Test
    void shouldThrowExceptionWhenPetCustomerIsEmpty(){
        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> petValidator.isPetOwner(pet, customer)
        );
        assertEquals("This pet does not belong to this customer", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOwnerPetIsEmpty(){
        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> petValidator.isPetOwner(pet, customer)
        );
        assertEquals("This pet does not belong to this customer", ex.getMessage());
        assertNull(pet.getOwner());
    }
}
