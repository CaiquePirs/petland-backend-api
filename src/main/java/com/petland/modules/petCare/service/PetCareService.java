package com.petland.modules.petCare.service;

import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.service.PetValidator;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.repositories.PetCareRepository;
import com.petland.modules.petCare.utils.PetCareServiceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public PetCare create(PetCareRequestDTO requestDTO) {
        Customer customer = customerService.findCustomerById(requestDTO.customerId());
        Pet pet = petService.findPetById(requestDTO.petId());
        Employee employee = employeeService.findById(requestDTO.employeeId());
        petValidator.isPetOwner(pet, customer);

        PetCare petCare = new PetCare();
        List<PetCareDetails> listServices = petCareDetailsService.createService(petCare, requestDTO.serviceDetailsList());
        BigDecimal totalRevenue = calculator.calculateTotalRevenueByServiceList(listServices);
        BigDecimal totalProfit = calculator.calculateTotalProfitByServiceList(listServices);

        petCare.setPetCareDetails(listServices);
        petCare.setTotalRevenue(totalRevenue);
        petCare.setTotalProfit(totalProfit);
        petCare.setLocation(requestDTO.location());
        petCare.setServiceDate(LocalDateTime.now());
        petCare.setPet(pet);
        petCare.setCustomer(customer);
        petCare.setEmployee(employee);

        customer.getServicesHistory().add(petCare);
        pet.getServicesHistory().add(petCare);
        return petCareRepository.save(petCare);
    }
}
