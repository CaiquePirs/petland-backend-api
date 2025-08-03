package com.petland.modules.petCare.utils;

import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PetCareServiceCalculator {

    public BigDecimal calculateTotalRevenue(int quantityService, BigDecimal costService){
        BigDecimal quantity = BigDecimal.valueOf(quantityService);
        return costService.multiply(quantity);
    }

    public BigDecimal calculateTotalRevenueByPetCareList(List<PetCare> petCareList){
        return petCareList.stream()
                .map(PetCare::getTotalRevenue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalCostOperatingByPetCareList(List<PetCare> petCareList){
        return petCareList.stream()
                .map(PetCare::getTotalCostOperating)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalProfitByPetCareList(List<PetCare> petCareList){
        return petCareList.stream()
                .map(PetCare::getTotalProfit)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalProfit(BigDecimal totalService, BigDecimal operatingCost){
        if(totalService == null || operatingCost == null){
            return BigDecimal.ZERO;
        }
        return totalService.subtract(operatingCost);
    }

    public BigDecimal calculateTotalRevenueByServiceList(List<PetCareDetails> petCareDetailsList){
        return petCareDetailsList.stream()
                .map(PetCareDetails::getTotalByService)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalProfitByServiceList(List<PetCareDetails> petCareDetailsList){
        return petCareDetailsList.stream()
                .map(PetCareDetails::getProfitByService)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalCostOperatingByServiceList(List<PetCareDetails> petCareDetailsList){
        return petCareDetailsList.stream()
                .map(PetCareDetails::getOperatingCost)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Integer sumAllItemsSold(List<PetCare> petCareList){
        return petCareList.stream()
                .mapToInt(details -> details.getPetCareDetails().size())
                .sum();
    }

    public Integer sumAllItemsSoldByServiceList(List<PetCareDetails> petCareDetails){
        return petCareDetails.stream()
                .mapToInt(PetCareDetails::getQuantityService)
                .sum();
    }
}
