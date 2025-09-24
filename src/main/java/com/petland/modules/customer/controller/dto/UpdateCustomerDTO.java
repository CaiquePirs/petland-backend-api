package com.petland.modules.customer.controller.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
@Schema(description = "DTO used to update customer information. All fields are optional.")
public record UpdateCustomerDTO(

        @Schema(description = "Updated full name of the customer.", example = "Johnathan Doe")
        String name,

        @Email(message = "Email must be valid")
        @Schema(description = "Updated email address of the customer.", example = "johnathan.doe@example.com")
        String email,

        @Length(min = 8, message = "Password must be up to 8 characters")
        @Schema(description = "Updated password for the customer account. Must be at least 8 characters.", example = "NewP@ssword123")
        String password,

        @Schema(description = "Updated phone number of the customer.", example = "+353871111111")
        String phone,

        @Schema(description = "Updated date of birth of the customer.", example = "1999-12-31")
        LocalDate dateBirth,

        @Schema(description = "Updated residential address of the customer.")
        UpdateAddressDTO addressDTO
) {}
