package com.petland.modules.customer.model;

import com.petland.common.entity.BaseEntity;
import com.petland.common.entity.Address;
import com.petland.common.entity.enums.Roles;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.sale.model.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

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

    @Column(nullable = false)
    private LocalDate dateBirth;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> myPets = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Consultation> consultationsHistory = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<PetCare> servicesHistory = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sale> salesHistory = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Embedded
    private Address address;
}

