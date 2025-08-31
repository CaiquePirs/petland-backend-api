package com.petland.sale;

import com.petland.modules.sale.dtos.ItemsSaleRequestDTO;
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

class ItemSaleRequestTest {

    private final Validator validator;

    public ItemSaleRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static Stream<Arguments> provideInvalidItemsSaleRequests() {
        return Stream.of(
                Arguments.of(null, 5, List.of("Product Id is required")),
                Arguments.of(UUID.randomUUID(), null, List.of("Product sales quantity must be informed")),
                Arguments.of(null, null, List.of("Product Id is required", "Product sales quantity must be informed"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidItemsSaleRequests")
    void shouldFailValidationForInvalidItemsSaleRequest(UUID productId, Integer productQuantity, List<String> expectedMessages) {
        ItemsSaleRequestDTO dto = new ItemsSaleRequestDTO(productId, productQuantity);

        Set<ConstraintViolation<ItemsSaleRequestDTO>> violations = validator.validate(dto);
        List<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        assertEquals(expectedMessages.size(), actualMessages.size());
        assertTrue(actualMessages.containsAll(expectedMessages));
    }

    @Test
    void shouldPassValidationForValidItemsSaleRequest() {
        ItemsSaleRequestDTO dto = new ItemsSaleRequestDTO(UUID.randomUUID(), 10);
        Set<ConstraintViolation<ItemsSaleRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
