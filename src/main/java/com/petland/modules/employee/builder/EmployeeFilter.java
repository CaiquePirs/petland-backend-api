package com.petland.modules.employee.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.enums.Department;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeFilter {

    private String name;
    private String email;
    private String phone;
    private Department department;
    private StatusEntity status;
}
