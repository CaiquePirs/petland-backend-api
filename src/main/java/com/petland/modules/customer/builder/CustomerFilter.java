package com.petland.modules.customer.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerFilter {
    private String name;
    private String email;
    private String phone;
    private StatusEntity status;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
