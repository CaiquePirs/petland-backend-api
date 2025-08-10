package com.petland.modules.consultation.dtos;

import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ConsultationHistoryResponseDTO(UUID id,
                                             UUID customerId,
                                             UUID petId,
                                             UUID employeeId,
                                             PetCareHistoryResponseDTO petCareResponse,
                                             SaleResponseDTO saleResponse,
                                             VaccinationResponseDTO vaccinationResponse,
                                             ConsultationDetailsHistoryDTO detailsResponse) {
}
