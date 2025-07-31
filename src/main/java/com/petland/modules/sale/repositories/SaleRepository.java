package com.petland.modules.sale.repositories;
import com.petland.modules.sale.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID>, JpaSpecificationExecutor<Sale>{
   Page<Sale> findByCustomerId(UUID customerId, Pageable pageable);
}
