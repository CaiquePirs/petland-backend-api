package com.petland.modules.petCare.utils;

import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.dtos.PetCareDetailsHistoryResponseDTO;
import com.petland.modules.petCare.dtos.PetCareDetailsResponseDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.mappers.PetCareDetailsMapper;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneratePetCareResponse {

    private final PetCareDetailsMapper mapper;

    public PetCareResponseDTO generate(PetCare petCare) {
        List<PetCareDetailsResponseDTO> detailsResponseList = generatePetCareDetailsList(
                petCare.getPetCareDetails());

        return PetCareResponseDTO.builder()
                .id(petCare.getId())
                .location(petCare.getLocation())
                .detailsResponseDTO(detailsResponseList)
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

    public List<PetCareHistoryResponseDTO> mapToCustomerServiceHistory(List<PetCare> petCareList) {
        return petCareList.stream().map(petCare -> {
            List<PetCareDetailsHistoryResponseDTO> serviceDetails = mapServiceDetails(petCare.getPetCareDetails());

            return PetCareHistoryResponseDTO.builder()
                    .id(petCare.getId())
                    .petId(petCare.getPet().getId())
                    .employeeId(petCare.getEmployee().getId())
                    .customerId(petCare.getCustomer().getId())
                    .location(petCare.getLocation())
                    .detailsResponseDTO(serviceDetails)
                    .serviceDate(petCare.getServiceDate())
                    .totalService(petCare.getTotalRevenue())
                    .build();
        }).toList();
    }
}
