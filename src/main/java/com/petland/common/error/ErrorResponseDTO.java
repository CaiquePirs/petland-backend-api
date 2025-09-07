package com.petland.common.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Generic error response returned by the API")
public record ErrorResponseDTO(
        @Schema(description = "HTTP status code of the error", example = "400")
        int status,

        @Schema(description = "Brief message summarizing the error", example = "A general error occurred")
        String message,

        @Schema(description = "List of detailed error messages", implementation = ErrorMessageDTO.class)
        List<ErrorMessageDTO> errors
) {}
