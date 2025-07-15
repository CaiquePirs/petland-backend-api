package com.petland.modules.customer.model;

import com.petland.common.Address;
import com.petland.enums.Roles;
import com.petland.enums.StatusEntity;
import com.petland.modules.attendance.model.Attendance;
import com.petland.modules.pet.Pet;
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
@Table(name = "tb_customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> myPets;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Attendance> serviceHistory;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Embedded
    private Address address;

    private UUID lastModifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntity status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

