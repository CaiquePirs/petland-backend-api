package com.petland.modules.vaccination.dto;

import com.petland.modules.vaccination.enums.VaccineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record VaccineRequestDTO(@NotBlank(message = "Lot number is required")
                                String lotNumber,

                                @NotBlank(message = "Supplier name is required")
                                String supplierName,

                                @NotNull(message = "Vaccine Type is required")
                                VaccineType vaccineType,

                                @NotNull(message = "Purchase price is required")
                                BigDecimal purchasePrice,

                                @NotNull(message = "Price sale is required")
                                BigDecimal priceSale,

                                @NotNull(message = "Stock Quantity is required")
                                Integer stockQuantity,

                                @NotNull(message = "Manufacture Date is required")
                                LocalDate manufactureDate,

                                @NotNull(message = "expiration Date is required")
                                LocalDate expirationDate) {
}
