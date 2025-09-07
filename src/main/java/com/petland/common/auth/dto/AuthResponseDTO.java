package com.petland.common.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO returned after successful authentication.")
public class AuthResponseDTO {

    @Schema(description = "JWT access token for API authentication.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String access_token;

    @Schema(description = "Token expiration time in seconds.", example = "3600")
    private Long expire_in;
}
