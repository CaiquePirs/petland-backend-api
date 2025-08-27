package com.petland.modules.pet.validator;

import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetValidator {

    public void isPetOwner(Pet pet, Customer customer) {
        if (customer.getMyPets().isEmpty() || pet.getOwner() == null || !customer.getMyPets().contains(pet)) {
            throw new UnauthorizedException("This pet does not belong to this customer");
        }
    }
}
