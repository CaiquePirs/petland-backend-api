package com.petland.modules.consultation.dtos;

import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Response DTO representing the historical record of a consultation, including details of related services.")
public record ConsultationHistoryResponseDTO(

        @Schema(description = "Unique identifier of the consultation history record.", example = "a3b1c5e6-9f8b-4c2d-b567-123456789abc")
        UUID id,

        @Schema(description = "Unique identifier of the customer.", example = "b4d2e7a8-6c9f-4f1d-b234-abcdef123456")
        UUID customerId,

        @Schema(description = "Unique identifier of the pet.", example = "c7e8f9d0-1b2a-4c3d-a123-654321abcdef")
        UUID petId,

        @Schema(description = "Unique identifier of the employee involved in the consultation.", example = "d9e1f2a3-4b5c-6d7e-8f9a-abcdef987654")
        UUID employeeId,

        @Schema(description = "Historical details of pet care services related to the consultation.")
        PetCareHistoryResponseDTO petCareResponse,

        @Schema(description = "Historical details of the sale associated with the consultation.")
        SaleResponseDTO saleResponse,

        @Schema(description = "Historical details of vaccinations performed during the consultation.")
        VaccinationResponseDTO vaccinationResponse,

        @Schema(description = "Detailed historical information about the consultation itself.")
        ConsultationDetailsHistoryDTO detailsResponse
) {}
