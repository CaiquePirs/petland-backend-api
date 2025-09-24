package com.petland.modules.consultation.mappers;

import com.petland.modules.consultation.controller.dtos.ConsultationDetailsHistoryDTO;
import com.petland.modules.consultation.controller.dtos.ConsultationHistoryResponseDTO;
import com.petland.modules.consultation.controller.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.petCare.controller.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.mappers.PetCareMapperGenerator;
import com.petland.modules.sale.controller.dtos.SaleResponseDTO;
import com.petland.modules.sale.mappers.SaleMapperGenerator;
import com.petland.modules.vaccination.controller.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.mappers.VaccinationMapperGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsultationMapperGenerator {

    private final PetCareMapperGenerator petCareResponse;
    private final SaleMapperGenerator saleResponse;
    private final VaccinationMapperGenerator vaccinationResponse;
    private final ConsultationMapper mapper;

    public ConsultationHistoryResponseDTO mapToCustomerHistory(Consultation consultation){
        SaleResponseDTO saleResponseDTO = Optional.ofNullable(consultation.getSales())
                .map(saleResponse::generateSaleResponse).orElse(null);

        VaccinationResponseDTO vaccinationResponseDTO = Optional.ofNullable(consultation.getVaccination())
                .map(vaccinationResponse::generate).orElse(null);

        PetCareHistoryResponseDTO petCareResponseDTO = Optional.ofNullable(consultation.getService())
                .map(petCareResponse::mapToCustomerServiceHistory).orElse(null);

        ConsultationDetailsHistoryDTO detailsHistoryDTO = mapper.toDetailsDTO(consultation.getDetails());
        return ConsultationHistoryResponseDTO.builder()
                .id(consultation.getId())
                .customerId(consultation.getCustomer().getId())
                .petId(consultation.getPet().getId())
                .employeeId(consultation.getEmployee().getId())
                .saleResponse(saleResponseDTO)
                .vaccinationResponse(vaccinationResponseDTO)
                .petCareResponse(petCareResponseDTO)
                .detailsResponse(detailsHistoryDTO)
                .build();
    }

    public ConsultationResponseDTO generateResponse(Consultation consultation) {
        SaleResponseDTO sale = Optional.ofNullable(consultation.getSales())
                .map(saleResponse::generateSaleResponse).orElse(null);

        VaccinationResponseDTO vaccination = Optional.ofNullable(consultation.getVaccination())
                .map(vaccinationResponse::generate).orElse(null);

        PetCareResponseDTO petCare = Optional.ofNullable(consultation.getService())
                .map(petCareResponse::generate).orElse(null);

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
