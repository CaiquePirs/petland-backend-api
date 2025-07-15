package com.petland.modules.pet;

import com.petland.enums.StatusEntity;
import com.petland.modules.attendance.model.Attendance;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.enums.PetGender;
import com.petland.modules.pet.enums.PetSpecies;
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
@Table(name = "tb_pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntity status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Attendance> serviceHistory;

    private UUID lastModifiedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
