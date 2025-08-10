package com.petland.modules.consultation.dtos;

import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConsultationRequestDTO(
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Pet ID is required")
        UUID petId,

        @NotNull(message = "Service details is required")
        ConsultationDetails details,

        SaleRequestDTO saleRequestDTO,
        VaccinationRequestDTO vaccinationRequestDTO,
        PetCareRequestDTO petCareRequestDTO) {
}
