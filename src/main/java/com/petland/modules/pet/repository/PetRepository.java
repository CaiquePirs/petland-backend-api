package com.petland.modules.pet.repository;

import com.petland.modules.pet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findPetByOwnerId(UUID customerId);
}
