package com.petland.modules.customer.dto;

import com.petland.common.entity.dto.UpdateAddressDTO;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
public record UpdateCustomerDTO(String name,
                                @Email(message = "Email must be valid")
                                String email,
                                @Length(min = 8, message = "Password must be up to 8 characters")
                                String password,
                                String phone,
                                LocalDate dateBirth,
                                UpdateAddressDTO addressDTO) {
}
