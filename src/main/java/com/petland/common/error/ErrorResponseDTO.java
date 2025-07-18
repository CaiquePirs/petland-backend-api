package com.petland.common.error;

import java.util.List;

public record ErrorResponseDTO(int status, String message, List<ErrorMessageDTO> errors) {
}
