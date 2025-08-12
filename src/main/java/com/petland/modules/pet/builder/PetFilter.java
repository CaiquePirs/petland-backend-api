package com.petland.modules.pet.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PetFilter {
    private String name;
    private String breed;
    private String specie;
    private String gender;
    private StatusEntity status;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
