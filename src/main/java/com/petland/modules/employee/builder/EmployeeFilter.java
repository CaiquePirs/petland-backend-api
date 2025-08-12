package com.petland.modules.employee.builder;

import com.petland.common.entity.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeFilter {

    private String name;
    private String email;
    private String phone;
    private String department;
    private StatusEntity status;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
