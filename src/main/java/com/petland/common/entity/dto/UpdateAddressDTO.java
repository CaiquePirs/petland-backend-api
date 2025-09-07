package com.petland.common.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO used to update address information. All fields are optional.")
public record UpdateAddressDTO(

        @Schema(description = "Street name of the address.", example = "Elm Street")
        String street,

        @Schema(description = "House or building number.", example = "1428")
        String number,

        @Schema(description = "Postal or ZIP code.", example = "12345-678")
        String zipCode,

        @Schema(description = "State or region of the address.", example = "California")
        String state,

        @Schema(description = "Country of the address.", example = "United States")
        String country,

        @Schema(description = "City of the address.", example = "Springwood")
        String city
) {}
