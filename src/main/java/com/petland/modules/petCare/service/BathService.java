package com.petland.modules.petCare.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.petCare.dtos.BathRequestDTO;
import com.petland.modules.petCare.mappers.BathMapper;
import com.petland.modules.petCare.model.Bath;
import com.petland.modules.petCare.repositories.BathRepository;
import com.petland.modules.petCare.utils.CalculateTotalCost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BathService {

    private final BathRepository bathRepository;
    private final BathMapper bathMapper;
    private final CalculateTotalCost calculateTotalCost;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;

    public Bath create(BathRequestDTO bathRequestDTO) {
        Pet pet = petService.findPetById(bathRequestDTO.petId());
        Customer customer = customerService.findCustomerById(bathRequestDTO.customerId());
        Employee employee = employeeService.findById(bathRequestDTO.employeeId());

        Bath bath = bathMapper.toEntity(bathRequestDTO);
        bath.setCustomer(customer);
        bath.setPet(pet);
        bath.setEmployee(employee);
        bath.setBathMoment(LocalDateTime.now());

        BigDecimal totalCost = calculateTotalCost.calculate(bathRequestDTO.bathQuantity(), bathRequestDTO.priceCost());
        bath.setTotalCost(totalCost);
        return bathRepository.save(bath);
    }

    public Bath findById(UUID bathId){
       return bathRepository.findById(bathId)
                .filter(b -> !b.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Bath ID not found"));
    }
}
