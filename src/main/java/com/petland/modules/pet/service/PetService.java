package com.petland.modules.pet.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final CustomerService customerService;
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final HttpServletRequest request;

    @Transactional
    public Pet create(PetRequestDTO petRequestDTO) {
        Customer customer = customerService
                .findCustomerById(petRequestDTO.customerId());

        Pet pet = petMapper.toEntity(petRequestDTO);
        pet.setOwner(customer);
        customer.getMyPets().add(pet);
        return petRepository.save(pet);
    }

    public Pet findPetById(UUID petId) {
       return petRepository.findById(petId)
                .filter(p -> !p.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    public void deletePetById(UUID petId) {
        Pet pet = findPetById(petId);
        String userCurrent = request.getAttribute("id").toString();

        if (!pet.getOwner().getId().equals(UUID.fromString(userCurrent)) && !request.isUserInRole("ADMIN")) {
            throw new UnauthorizedException("Unauthorized user");
        }

        pet.setStatus(StatusEntity.DELETED);
        petRepository.save(pet);
    }

    public List<Pet> getPetsByCustomerId(UUID customerId){
       customerService.findCustomerById(customerId);
       return petRepository.findPetByOwnerId(customerId)
                .stream()
                .filter(pet -> !pet.getStatus().equals(StatusEntity.DELETED))
                .toList();
    }

}
