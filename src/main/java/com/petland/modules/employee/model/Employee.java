package com.petland.modules.employee.model;


import com.petland.common.Address;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.enums.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Roles> roles;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Department department;

    @Column(nullable = false)
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private StatusEntity status;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @Column(nullable = false)
    private UUID employee_audit;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}