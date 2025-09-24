package com.petland.modules.pet.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.enums.PetGender;
import com.petland.modules.pet.model.enums.PetSpecies;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.vaccination.module.Vaccination;
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
@Table(name = "tb_pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpecies specie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double weight;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Consultation> consultationsHistory = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetCare> servicesHistory = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Vaccination> vaccinationsHistory = new ArrayList<>();
}
