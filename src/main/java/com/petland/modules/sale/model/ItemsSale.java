package com.petland.modules.sale.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.product.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_items_sale")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsSale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal productPrice;

    private int productQuantity;

    private BigDecimal itemsSaleTotal;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
}
