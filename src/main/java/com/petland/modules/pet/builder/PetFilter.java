package com.petland.modules.pet.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PetFilter {
    private String name;
    private String breed;
    private PetSpecies specie;
    private PetGender gender;
    private StatusEntity status;
}
