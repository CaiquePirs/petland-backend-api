package com.petland.modules.petCare.service;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import com.petland.modules.petCare.builder.PetCareFilter;
import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.petCare.specifications.PetCareSpecification;
import com.petland.modules.petCare.utils.GeneratePetCareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetCareService {

    private final PetService petService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetValidator petValidator;
    private final PetCareCalculator calculator;
    private final PetCareRepository repository;
    private final PetCareDetailsService petCareDetailsService;
    private final GeneratePetCareResponse generateResponse;

    @Transactional
    public PetCare register(PetCareRequestDTO requestDTO) {
        Customer customer = customerService.findById(requestDTO.customerId());
        Pet pet = petService.findById(requestDTO.petId());
        Employee employee = employeeService.findById(requestDTO.employeeId());
        petValidator.isPetOwner(pet, customer);

        List<PetCareDetails> listServices = petCareDetailsService.createService(requestDTO.serviceDetailsList());
        BigDecimal totalRevenue = calculator.calculateTotalRevenueByServiceList(listServices);
        BigDecimal totalProfit = calculator.calculateTotalProfitByServiceList(listServices);
        BigDecimal totalCostOperating = calculator.calculateTotalCostOperatingByServiceList(listServices);

        PetCare petCare = PetCare.builder()
                .petCareDetails(listServices)
                .totalRevenue(totalRevenue)
                .totalProfit(totalProfit)
                .totalCostOperating(totalCostOperating)
                .location(requestDTO.location())
                .serviceDate(LocalDateTime.now())
                .pet(pet)
                .employee(employee)
                .customer(customer)
                .build();

        listServices.forEach(d -> d.setPetCare(petCare));
        customer.getServicesHistory().add(petCare);
        pet.getServicesHistory().add(petCare);
        return repository.save(petCare);
    }

    public Page<PetCareHistoryResponseDTO> findAllByCustomerId(UUID customerId, Pageable pageable){
        Page<PetCare> petCareList = repository.findByCustomerId(customerId, pageable);

        if(petCareList.getContent().isEmpty()){
            throw new NotFoundException("Customer service history list not found");
        }
        return petCareList.map(generateResponse::mapToCustomerServiceHistory);
    }

    public PetCare findById(UUID petCareId){
        return repository.findById(petCareId)
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
        repository.save(petCare);
    }

    public Page<PetCareResponseDTO> findAllByFilter(PetCareFilter filter, Pageable pageable){
        return repository.findAll(PetCareSpecification.specification(filter), pageable)
                .map(generateResponse::generate);
    }

    public List<PetCare> findAllByPeriod(LocalDate dateMin, LocalDate dateMax){
        return repository.findAll(PetCareSpecification.reportSpecification(dateMin, dateMax)).stream()
                        .filter(p -> !p.getStatus().equals(StatusEntity.DELETED)).toList();
    }

    public List<PetCare> findAllByPetCareType(PetCareType petCareType){
        return repository.findAll(PetCareSpecification.findByServiceType(petCareType)).stream()
                        .filter(p -> !p.getStatus().equals(StatusEntity.DELETED)).toList();
    }
}
