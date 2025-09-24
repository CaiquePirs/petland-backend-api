package com.petland.modules.customer.controller.dto;

import com.petland.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
@Schema(description = "Request DTO used to create a new customer.")
public record CustomerRequestDTO(

        @NotBlank(message = "Name is required")
        @Schema(description = "Full name of the customer.", example = "John Doe")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Email address of the customer.", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Length(min = 8, message = "Password must be up to 8 characters")
        @Schema(description = "Password for the customer account. Must be at least 8 characters.", example = "StrongP@ss123")
        String password,

        @NotBlank(message = "Phone is required")
        @Schema(description = "Phone number of the customer.", example = "+353871234567")
        String phone,

        @NotNull(message = "DateBirth is required")
        @PastOrPresent(message = "The date cannot be in the future")
        @Schema(description = "Date of birth of the customer. Cannot be in the future.", example = "2000-05-15")
        LocalDate dateBirth,

        @NotNull(message = "Address is required")
        @Valid
        @Schema(description = "Residential address of the customer.")
        Address address
) {}
