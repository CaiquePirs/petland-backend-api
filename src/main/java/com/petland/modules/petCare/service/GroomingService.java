package com.petland.modules.petCare.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.petCare.dtos.GroomingRequestDTO;
import com.petland.modules.petCare.mappers.GroomingMapper;
import com.petland.modules.petCare.model.Grooming;
import com.petland.modules.petCare.repositories.GroomingRepository;
import com.petland.modules.petCare.utils.CalculateTotalCost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroomingService {

    private final PetService petService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final GroomingMapper groomingMapper;
    private final CalculateTotalCost calculateTotalCost;
    private final GroomingRepository groomingRepository;

    public Grooming create(GroomingRequestDTO groomingRequestDTO) {
        Pet pet = petService.findPetById(groomingRequestDTO.petId());
        Customer customer = customerService.findCustomerById(groomingRequestDTO.customerId());
        Employee employee = employeeService.findById(groomingRequestDTO.employeeId());

        Grooming grooming = groomingMapper.toEntity(groomingRequestDTO);
        grooming.setCustomer(customer);
        grooming.setPet(pet);
        grooming.setEmployee(employee);
        grooming.setGroomingMoment(LocalDateTime.now());

        BigDecimal totalCost = calculateTotalCost.calculate(groomingRequestDTO.groomingQuantity(), groomingRequestDTO.priceCost());
        grooming.setTotalCost(totalCost);
        return groomingRepository.save(grooming);
    }

    public Grooming findById(UUID groomingId){
        return groomingRepository.findById(groomingId)
                .filter(g -> !g.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Grooming ID not found"));
    }

    public void deleteById(UUID groomingId){
        Grooming grooming = findById(groomingId);
        grooming.setStatus(StatusEntity.DELETED);
        groomingRepository.save(grooming);
    }
}
