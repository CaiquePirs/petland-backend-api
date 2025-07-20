package com.petland.modules.sale.repositories;

import com.petland.modules.sale.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
}
