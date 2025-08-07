package com.petland.modules.pet.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.dto.PetUpdateDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.pet.specifications.PetSpecification;
import com.petland.modules.pet.validator.PetUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AccessValidator accessValidator;
    private final PetUpdateValidator petUpdateValidator;

    @Transactional
    public Pet create(PetRequestDTO petRequestDTO) {
        Customer customer = customerService
                .findById(petRequestDTO.customerId());

        Pet pet = petMapper.toEntity(petRequestDTO);
        pet.setOwner(customer);
        customer.getMyPets().add(pet);
        return petRepository.save(pet);
    }

    public Pet findById(UUID petId) {
       return petRepository.findById(petId)
                .filter(p -> !p.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    public void deactivateById(UUID petId) {
        Pet pet = findById(petId);
        accessValidator.isOwnerOrAdmin(pet.getOwner().getId());
        pet.setStatus(StatusEntity.DELETED);
        petRepository.save(pet);
    }

    public List<Pet> getPetsByCustomerId(UUID customerId){
       customerService.findById(customerId);
       return petRepository.findPetByOwnerId(customerId)
                .stream()
                .filter(pet -> !pet.getStatus().equals(StatusEntity.DELETED))
                .toList();
    }

    public Page<PetResponseDTO> listAllByFilter(String name, String specie, String gender, String breed, StatusEntity status, Pageable pageable){
      Page<Pet> petsByFilter = petRepository.findAll(PetSpecification.filterBy(name, specie, gender, breed,status), pageable);
      return petsByFilter.map(petMapper::toDTO);
    }

    public Pet updateById(UUID petId, PetUpdateDTO petUpdate){
        Pet pet = findById(petId);
        accessValidator.isOwnerOrAdmin(pet.getOwner().getId());
        pet = petUpdateValidator.validate(petUpdate, pet);
        return petRepository.save(pet);
    }

}
