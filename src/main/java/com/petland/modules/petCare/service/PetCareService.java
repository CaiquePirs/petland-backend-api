package com.petland.modules.petCare.service;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.service.PetValidator;
import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import com.petland.modules.petCare.utils.PetCareServiceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetCareService {

    private final PetService petService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetValidator petValidator;
    private final PetCareServiceCalculator calculator;
    private final PetCareRepository petCareRepository;
    private final PetCareDetailsService petCareDetailsService;
    private final GeneratePetCareResponse generate;

    @Transactional
    public PetCare register(PetCareRequestDTO requestDTO) {
        Customer customer = customerService.findById(requestDTO.customerId());
        Pet pet = petService.findById(requestDTO.petId());
        Employee employee = employeeService.findById(requestDTO.employeeId());
        petValidator.isPetOwner(pet, customer);

        PetCare petCare = new PetCare();
        List<PetCareDetails> listServices = petCareDetailsService.createService(petCare, requestDTO.serviceDetailsList());
        BigDecimal totalRevenue = calculator.calculateTotalRevenueByServiceList(listServices);
        BigDecimal totalProfit = calculator.calculateTotalProfitByServiceList(listServices);
        BigDecimal totalCostOperating = calculator.calculateTotalCostOperatingByServiceList(listServices);

        petCare.setPetCareDetails(listServices);
        petCare.setTotalRevenue(totalRevenue);
        petCare.setTotalProfit(totalProfit);
        petCare.setTotalCostOperating(totalCostOperating);
        petCare.setLocation(requestDTO.location());
        petCare.setServiceDate(LocalDateTime.now());
        petCare.setPet(pet);
        petCare.setCustomer(customer);
        petCare.setEmployee(employee);

        customer.getServicesHistory().add(petCare);
        pet.getServicesHistory().add(petCare);
        return petCareRepository.save(petCare);
    }

    public Page<PetCareHistoryResponseDTO> findAllByCustomerId(UUID customerId, Pageable pageable){
        Page<PetCare> petCareList = petCareRepository.findByCustomerId(customerId, pageable);

        if(petCareList.getContent().isEmpty()){
            throw new NotFoundException("Customer service history list not found");
        }
        List<PetCareHistoryResponseDTO> serviceHistoryList = generate.mapToCustomerServiceHistory(petCareList.getContent());

        return new PageImpl<>(serviceHistoryList, pageable, serviceHistoryList.size());
    }

    public PetCare findById(UUID petCareId){
        return petCareRepository.findById(petCareId)
                .filter(p -> !p.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("PetCare ID not found"));
    }

    public void deactivateById(UUID petCareID){
        PetCare petCare = findById(petCareID);

        if(!petCare.getPetCareDetails().isEmpty()){
            for(PetCareDetails details : petCare.getPetCareDetails()){
                details.setStatus(StatusEntity.DELETED);
            }
        }
        petCare.setStatus(StatusEntity.DELETED);
        petCareRepository.save(petCare);
    }
}
