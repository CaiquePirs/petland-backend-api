package com.petland.modules.sale.repositories;

import com.petland.modules.sale.model.ItemsSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemsSaleRepository extends JpaRepository<ItemsSale, UUID> {
}
