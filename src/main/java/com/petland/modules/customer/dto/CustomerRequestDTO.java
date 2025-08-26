package com.petland.modules.customer.dto;

import com.petland.common.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
public record CustomerRequestDTO(@NotBlank(message = "Name is required")
                                 String name,

                                 @NotBlank(message = "Email is required")
                                 @Email(message = "Email must be valid")
                                 String email,

                                 @NotBlank(message = "Password is required")
                                 @Length(min = 8, message = "Password must be up to 8 characters")
                                 String password,

                                 @NotBlank(message = "Phone is required")
                                 String phone,

                                 @NotNull(message = "DateBirth is required")
                                 @PastOrPresent(message = "The date cannot be in the future")
                                 LocalDate dateBirth,

                                 @NotNull(message = "Address is required")
                                 @Valid
                                 Address address) {
}
