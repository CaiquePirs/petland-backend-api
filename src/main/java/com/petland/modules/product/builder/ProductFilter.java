package com.petland.modules.product.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.product.enums.ProductType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProductFilter {

    private String name;
    private String brand;
    private ProductType productType;
    private LocalDate manufactureDateMin;
    private LocalDate expirationDateMax;
    private BigDecimal costSaleMin;
    private Integer stockMin;
    private StatusEntity status;

}
