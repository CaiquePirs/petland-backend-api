package com.petland.common.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for authentication (login).")
public record AuthRequestDTO(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Email address of the user.", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(description = "Password of the user account.", example = "StrongP@ssword123")
        String password
) {}
