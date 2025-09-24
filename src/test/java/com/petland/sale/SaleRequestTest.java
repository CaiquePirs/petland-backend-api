package com.petland.sale;

import com.petland.modules.consultation.model.enums.PaymentType;
import com.petland.modules.sale.controller.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SaleRequestTest {

    private final Validator validator;

    public SaleRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static Stream<Arguments> provideInvalidSaleRequests() {
        UUID validCustomerId = UUID.randomUUID();
        List<ItemsSaleRequestDTO> validItems = List.of(ItemsSaleRequestDTO.builder().build());
        PaymentType validPayment = PaymentType.CREDIT_CARD;

        return Stream.of(
                Arguments.of(null, validItems, validPayment, List.of("Customer Id is required")),
                Arguments.of(validCustomerId, null, validPayment, List.of("An item of a sale must be informed in the requisition")),
                Arguments.of(validCustomerId, validItems, null, List.of("Payment type is required")),
                Arguments.of(null, null, null, List.of(
                        "Customer Id is required",
                        "An item of a sale must be informed in the requisition",
                        "Payment type is required"
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSaleRequests")
    void shouldFailValidationForInvalidSaleRequest(UUID customerId,
                                                   List<ItemsSaleRequestDTO> items,
                                                   PaymentType paymentType,
                                                   List<String> expectedMessages) {

        SaleRequestDTO dto = SaleRequestDTO.builder()
                .customerId(customerId)
                .itemsSaleRequestDTO(items)
                .paymentType(paymentType)
                .build();

        Set<ConstraintViolation<SaleRequestDTO>> violations = validator.validate(dto);

        List<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertEquals(expectedMessages.size(), actualMessages.size());
        assertTrue(actualMessages.containsAll(expectedMessages));
    }

    @Test
    void shouldPassValidationForValidSaleRequest() {
        SaleRequestDTO dto = SaleRequestDTO.builder()
                .customerId(UUID.randomUUID())
                .itemsSaleRequestDTO(List.of(ItemsSaleRequestDTO.builder().build()))
                .paymentType(PaymentType.BANK_TRANSFER)
                .build();

        Set<ConstraintViolation<SaleRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
