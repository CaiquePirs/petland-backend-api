package com.petland.modules.consultation.validator;

import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.validator.PetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsultationValidator {

    private final PetValidator petValidator;

    public void validateIfItIsTheSameCustomer(ConsultationRequestDTO attendanceRequest, Customer customer, Pet pet) {
        petValidator.isPetOwner(pet, customer);

        boolean itsTheSameCustomer =
                Optional.ofNullable(attendanceRequest.saleRequestDTO())
                        .map(sale -> sale.customerId().equals(attendanceRequest.customerId()))
                        .orElse(false)
                ||
                Optional.ofNullable(attendanceRequest.vaccinationRequestDTO())
                        .map(vaccination -> vaccination.customerId().equals(attendanceRequest.customerId()))
                        .orElse(false)
                ||
                Optional.ofNullable(attendanceRequest.petCareRequestDTO())
                        .map(petCare -> petCare.customerId().equals(attendanceRequest.customerId()))
                        .orElse(false);
        if (!itsTheSameCustomer) {
            throw new UnauthorizedException("The sale must be registered to the same customer as the one associated with the consultation.");
        }
    }
}
