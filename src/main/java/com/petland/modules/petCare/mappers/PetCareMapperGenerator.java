package com.petland.modules.petCare.mappers;

import com.petland.modules.petCare.controller.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareDetailsHistoryResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareDetailsResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PetCareMapperGenerator {

    private final PetCareDetailsMapper mapper;

    public PetCareResponseDTO generate(PetCare petCare) {
        return PetCareResponseDTO.builder()
                .id(petCare.getId())
                .location(petCare.getLocation())
                .detailsResponseDTO(generatePetCareDetailsList(petCare.getPetCareDetails()))
                .totalRevenue(petCare.getTotalRevenue())
                .totalProfit(petCare.getTotalProfit())
                .totalCostOperating(petCare.getTotalCostOperating())
                .employeeId(petCare.getEmployee().getId())
                .customerId(petCare.getCustomer().getId())
                .petId(petCare.getPet().getId())
                .serviceDate(petCare.getServiceDate())
                .build();
    }

    public List<PetCareDetailsResponseDTO> generatePetCareDetailsList(List<PetCareDetails> petCareDetailsList) {
        return petCareDetailsList.stream().map(mapper::toDTO).toList();
    }

    public List<PetCareDetailsHistoryResponseDTO> mapServiceDetails(List<PetCareDetails> serviceDetails) {
        return serviceDetails.stream()
                .map(details -> PetCareDetailsHistoryResponseDTO.builder()
                        .id(details.getId())
                        .petCareType(details.getPetCareType())
                        .noteService(details.getNoteService())
                        .unitPrice(details.getUnitPrice())
                        .quantityService(details.getQuantityService())
                        .totalByService(details.getTotalByService())
                        .petCareId(details.getPetCare().getId())
                        .build())
                .toList();
    }

    public PetCareHistoryResponseDTO mapToCustomerServiceHistory(PetCare petCare) {
        return PetCareHistoryResponseDTO.builder()
                .id(petCare.getId())
                .petId(petCare.getPet().getId())
                .employeeId(petCare.getEmployee().getId())
                .customerId(petCare.getCustomer().getId())
                .location(petCare.getLocation())
                .detailsResponseDTO(mapServiceDetails(petCare.getPetCareDetails()))
                .serviceDate(petCare.getServiceDate())
                .totalService(petCare.getTotalRevenue())
                .build();
    }
}
