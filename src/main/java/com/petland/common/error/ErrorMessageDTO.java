package com.petland.common.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed message of a specific error")
public record ErrorMessageDTO(
        @Schema(description = "Field or parameter that caused the error, if applicable", example = "fieldName")
        String field,

        @Schema(description = "Description of the specific error", example = "Description of the error")
        String error
) {}

