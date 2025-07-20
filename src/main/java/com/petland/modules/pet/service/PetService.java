package com.petland.modules.pet.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.UnauthorizedException;
import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
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
    private final AccessValidator accessValidator;
    private final PetMapper petMapper;

    @Transactional
    public Pet create(PetRequestDTO petRequestDTO) {
        Customer customer = customerService
                .findCustomerById(petRequestDTO.customerId());

        UUID userLogged = accessValidator.getLoggedInUser();

        Pet pet = petMapper.toEntity(petRequestDTO);
        pet.setOwner(customer);
        pet.setStatus(StatusEntity.ACTIVE);
        pet.setLastModifiedBy(userLogged);

        customer.getMyPets().add(pet);
        return petRepository.save(pet);
    }

    public Pet findPetById(UUID petId, UUID ownerId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found"));

        if (pet.getStatus().equals(StatusEntity.DELETED)) {
            throw new NotFoundException("Pet not found");
        }

        Customer owner = pet.getOwner();

        if (!owner.getId().equals(ownerId)) {
            throw new UnauthorizedException("Unauthorized user");
        }

        return pet;
    }

}
