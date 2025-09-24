package com.petland.modules.vaccination.module;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.vaccination.module.enums.VaccineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_vaccines")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vaccine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String lotNumber;

    @Column(nullable = false)
    private String supplierName;

    @Enumerated(value = EnumType.STRING)
    private VaccineType vaccineType;

    @Column(nullable = false)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private BigDecimal priceSale;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expirationDate;
}
