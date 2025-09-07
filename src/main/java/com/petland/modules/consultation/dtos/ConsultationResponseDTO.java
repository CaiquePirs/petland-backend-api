package com.petland.modules.consultation.dtos;

import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Response DTO representing a consultation, including related services and details.")
public record ConsultationResponseDTO(

        @Schema(description = "Unique identifier of the consultation.", example = "d8c3b0e2-6b45-4a91-9d11-7abc9e765432")
        UUID id,

        @Schema(description = "Unique identifier of the customer.", example = "f4a1e8b7-2d45-4b8f-9c21-abcdef987654")
        UUID customerId,

        @Schema(description = "Unique identifier of the pet.", example = "b2d7a4c8-1f23-45b6-9a12-1234567890ab")
        UUID petId,

        @Schema(description = "Unique identifier of the employee (veterinarian or staff).", example = "a1b2c3d4-5e6f-7a8b-9c0d-123abc456def")
        UUID employeeId,

        @Schema(description = "Details about the sale related to the consultation.")
        SaleResponseDTO saleResponseDTO,

        @Schema(description = "Details about vaccination applied during the consultation.")
        VaccinationResponseDTO vaccinationResponseDTO,

        @Schema(description = "Details about pet care services related to the consultation.")
        PetCareResponseDTO petCareResponseDTO,

        @Schema(description = "Additional details about the consultation.")
        ConsultationDetails details
) {}
