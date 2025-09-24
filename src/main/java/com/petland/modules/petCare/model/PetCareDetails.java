package com.petland.modules.petCare.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.petCare.model.enums.PetCareType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Table(name = "tb_petcare_details")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCareDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    PetCareType petCareType;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantityService;

    @Column(nullable = false)
    private BigDecimal operatingCost;

    @Column(nullable = false)
    private BigDecimal totalByService;

    @Column(nullable = false)
    private BigDecimal profitByService;

    @Column(nullable = false)
    private String noteService;

    @ManyToOne
    @JoinColumn(name = "petcare_id")
    private PetCare petCare;
}
