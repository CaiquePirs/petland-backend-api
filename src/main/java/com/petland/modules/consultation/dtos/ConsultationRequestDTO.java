package com.petland.modules.consultation.dtos;

import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Request DTO used to create a new consultation with optional related services.")
public record ConsultationRequestDTO(

        @NotNull(message = "Customer ID is required")
        @Schema(description = "Unique identifier of the customer.", example = "a3b1c5e6-9f8b-4c2d-b567-123456789abc")
        UUID customerId,

        @NotNull(message = "Pet ID is required")
        @Schema(description = "Unique identifier of the pet.", example = "c7e8f9d0-1b2a-4c3d-a123-654321abcdef")
        UUID petId,

        @NotNull(message = "Service details is required")
        @Schema(description = "Mandatory consultation details such as type, status, payment, and priority.")
        ConsultationDetails details,

        @Schema(description = "Optional sale details related to the consultation.")
        SaleRequestDTO saleRequestDTO,

        @Schema(description = "Optional vaccination details related to the consultation.")
        VaccinationRequestDTO vaccinationRequestDTO,

        @Schema(description = "Optional pet care details related to the consultation.")
        PetCareRequestDTO petCareRequestDTO
) {}
