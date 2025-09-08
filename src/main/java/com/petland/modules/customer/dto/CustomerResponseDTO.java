package com.petland.modules.customer.dto;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Schema(description = "Response DTO containing customer details.")
public record CustomerResponseDTO(

        @Schema(description = "Unique identifier of the customer.", example = "a3b1c5e6-9f8b-4c2d-b567-123456789abc")
        UUID id,

        @Schema(description = "Full name of the customer.", example = "John Doe")
        String name,

        @Schema(description = "Email address of the customer.", example = "john.doe@example.com")
        String email,

        @Schema(description = "Phone number of the customer.", example = "+353871234567")
        String phone,

        @Schema(description = "Date of birth of the customer.", example = "2000-05-15")
        LocalDate dateBirth,

        @Schema(description = "Residential address of the customer.")
        Address address
) {}
