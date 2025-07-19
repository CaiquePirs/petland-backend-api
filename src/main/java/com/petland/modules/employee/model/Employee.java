package com.petland.modules.employee.model;

import com.petland.utils.Address;
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

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Department department;

    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private LocalDate dateBirth;

    private UUID employee_audit;

    @Enumerated(value = EnumType.STRING)
    private StatusEntity status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}