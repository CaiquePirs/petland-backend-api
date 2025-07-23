package com.petland.modules.pet.service;

import com.petland.common.exception.UnauthorizedException;
import com.petland.common.exception.NotFoundException;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.repository.EmployeeRepository;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final CustomerService customerService;
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Pet create(PetRequestDTO petRequestDTO) {
        Customer customer = customerService
                .findCustomerById(petRequestDTO.customerId());

        Pet pet = petMapper.toEntity(petRequestDTO);
        pet.setOwner(customer);
        pet.setStatus(StatusEntity.ACTIVE);

        customer.getMyPets().add(pet);
        return petRepository.save(pet);
    }

    public Pet findPetById(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found"));

        if (pet.getStatus().equals(StatusEntity.DELETED)) {
            throw new NotFoundException("Pet not found");
        }

        return pet;
    }

}
