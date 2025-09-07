package com.petland.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a physical address.")
public class Address {

    @Schema(description = "Street name of the address.", example = "Baker Street")
    private String street;

    @Schema(description = "House or building number.", example = "221B")
    private String number;

    @Schema(description = "Postal or ZIP code.", example = "NW1 6XE")
    private String zipCode;

    @Schema(description = "State or region of the address.", example = "Greater London")
    private String state;

    @Schema(description = "Country of the address.", example = "United Kingdom")
    private String country;

    @Schema(description = "City of the address.", example = "London")
    private String city;
}
