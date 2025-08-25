package com.petland.modules.consultation.validator;

import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsultationValidator {

    public void validateIfItIsTheSameCustomer(ConsultationRequestDTO consultationRequest) {
        UUID customerId = consultationRequest.customerId();

        boolean itsTheSameCustomer =
                Optional.ofNullable(consultationRequest.saleRequestDTO())
                        .map(sale -> sale.customerId().equals(customerId))
                        .orElse(true)
                &&
                Optional.ofNullable(consultationRequest.vaccinationRequestDTO())
                        .map(vaccination -> vaccination.customerId().equals(customerId))
                        .orElse(true)
                &&
                Optional.ofNullable(consultationRequest.petCareRequestDTO())
                        .map(petCare -> petCare.customerId().equals(customerId))
                        .orElse(true);

        if (!itsTheSameCustomer) {
            throw new UnauthorizedException("The sale must be registered to the same customer as the one associated with the consultation.");
        }
    }
}
