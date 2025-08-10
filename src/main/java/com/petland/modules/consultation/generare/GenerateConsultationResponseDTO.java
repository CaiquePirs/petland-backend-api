package com.petland.modules.consultation.generare;

import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.util.GenerateSaleResponse;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateConsultationResponseDTO {

    private final GeneratePetCareResponse petCareResponse;
    private final GenerateSaleResponse saleResponse;
    private final GenerateVaccinationResponse vaccinationResponse;

    public ConsultationResponseDTO generate(Consultation consultation) {
        SaleResponseDTO sale = null;
        VaccinationResponseDTO vaccination = null;
        PetCareResponseDTO petCare = null;

        if (consultation.getVaccination() != null) {
            vaccination = vaccinationResponse.generate(consultation.getVaccination());
        }

        if (consultation.getSales() != null) {
            sale = saleResponse.generateSaleResponse(consultation.getSales());
        }

        if (consultation.getService() != null) {
            petCare = petCareResponse.generate(consultation.getService());
        }

        return ConsultationResponseDTO.builder()
                .id(consultation.getId())
                .customerId(consultation.getCustomer().getId())
                .petId(consultation.getPet().getId())
                .employeeId(consultation.getEmployee().getId())
                .saleResponseDTO(sale)
                .vaccinationResponseDTO(vaccination)
                .petCareResponseDTO(petCare)
                .details(consultation.getDetails())
                .build();
    }

}
