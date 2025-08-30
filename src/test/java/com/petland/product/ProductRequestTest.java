package com.petland.product;

import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.enums.ProductType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    static Stream<Arguments> generateInvalidArguments() {
        LocalDate today = LocalDate.now();

        return Stream.of(
                Arguments.of(null, "Description", "Brand", "Supplier", ProductType.ACCESSORIES, today, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Name is required"),

                Arguments.of("Product", null, "Brand", "Supplier", ProductType.ACCESSORIES, today, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Description is required"),

                Arguments.of("Product", "Description", null, "Supplier", ProductType.ACCESSORIES, today, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Brand is required"),

                Arguments.of("Product", "Description", "Brand", null, ProductType.ACCESSORIES, today, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Supplier name is required"),

                Arguments.of("Product", "Description", "Brand", "Supplier", null, today, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Product type is required"),

                Arguments.of("Product", "Description", "Brand", "Supplier", ProductType.ACCESSORIES, null, today.plusDays(30),
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Manufacture date is required"),

                Arguments.of("Product", "Description", "Brand", "Supplier", ProductType.ACCESSORIES, today, null,
                        BigDecimal.TEN, BigDecimal.ONE, 10, "Expiration date is required"),

                Arguments.of("Product", "Description", "Brand", "Supplier", ProductType.ACCESSORIES, today, today.plusDays(30),
                        null, BigDecimal.ONE, 10, "Cost price is required"),

                Arguments.of("Product", "Description", "Brand", "Supplier", ProductType.ACCESSORIES, today, today.plusDays(30),
                        BigDecimal.TEN, null, 10, "Cost sale is required")
        );
    }

    @ParameterizedTest(name = "Test {index}: expected message = {10}")
    @MethodSource("generateInvalidArguments")
    void shouldValidateInvalidArguments(String name, String description, String brand, String supplierName,
                                        ProductType productType, LocalDate manufactureDate, LocalDate expirationDate,
                                        BigDecimal costPrice, BigDecimal costSale, int stockQuantity, String expectedMessage) {

        ProductRequestDTO dto = new ProductRequestDTO(name, description, brand, supplierName, productType,
                manufactureDate, expirationDate, costPrice, costSale, stockQuantity
        );

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Expected at least one violation");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(expectedMessage)),
                "Expected message: " + expectedMessage);
    }

    @Test
    void shouldAcceptRequestWithValidValues() {
        UUID id = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        ProductRequestDTO dto = new ProductRequestDTO(
                "Product Name",
                "Description",
                "Brand",
                "Supplier",
                ProductType.ACCESSORIES,
                today,
                today.plusDays(30),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0),
                100
        );

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "There should be no violations for a valid DTO");
        assertAll(
                () -> assertEquals("Product Name", dto.name()),
                () -> assertEquals("Description", dto.description()),
                () -> assertEquals("Brand", dto.brand()),
                () -> assertEquals("Supplier", dto.supplierName()),
                () -> assertEquals(ProductType.ACCESSORIES, dto.productType()),
                () -> assertEquals(today, dto.manufactureDate()),
                () -> assertEquals(today.plusDays(30), dto.expirationDate()),
                () -> assertEquals(BigDecimal.valueOf(10.0), dto.costPrice()),
                () -> assertEquals(BigDecimal.valueOf(15.0), dto.costSale()),
                () -> assertEquals(100, dto.stockQuantity())
        );
    }
}

