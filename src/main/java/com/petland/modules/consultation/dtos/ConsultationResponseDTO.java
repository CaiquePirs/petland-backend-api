package com.petland.modules.consultation.dtos;

import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ConsultationResponseDTO(UUID id,
                                      UUID customerId,
                                      UUID petId,
                                      UUID employeeId,
                                      SaleResponseDTO saleResponseDTO,
                                      VaccinationResponseDTO vaccinationResponseDTO,
                                      PetCareResponseDTO petCareResponseDTO,
                                      ConsultationDetails details) {
}
