package com.petland.modules.pet.validator;

import com.petland.modules.pet.controller.dto.PetUpdateDTO;
import com.petland.modules.pet.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetUpdateValidator {

    public Pet validate(PetUpdateDTO petUpdateDTO, Pet pet){
        if(petUpdateDTO.age() != null) pet.setAge(petUpdateDTO.age());
        if(petUpdateDTO.weight() != null) pet.setWeight(petUpdateDTO.weight());
        if(petUpdateDTO.breed() != null && !petUpdateDTO.breed().isBlank()) pet.setBreed(petUpdateDTO.breed());
        if(petUpdateDTO.name() != null && !petUpdateDTO.name().isBlank()) pet.setName(petUpdateDTO.name());
        if(petUpdateDTO.specie() != null && !petUpdateDTO.specie().toString().isBlank()) pet.setSpecie(petUpdateDTO.specie());
        if(petUpdateDTO.gender() != null && !petUpdateDTO.gender().toString().isBlank()) pet.setGender(petUpdateDTO.gender());
        if(petUpdateDTO.dateBirth() != null) pet.setDateBirth(petUpdateDTO.dateBirth());
        return pet;
    }
}
