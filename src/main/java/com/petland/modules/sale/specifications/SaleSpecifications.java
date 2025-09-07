package com.petland.modules.sale.specifications;

import com.petland.modules.product.model.Product;
import com.petland.modules.sale.builder.SaleFilter;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleSpecifications {

    public static Specification<Sale> specifications(SaleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getEmployeeId() != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), filter.getEmployeeId()));
            }
            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), filter.getCustomerId()));
            }
            if (filter.getPaymentType() != null) {
                predicates.add(cb.equal(root.get("paymentType"), filter.getPaymentType()));
            }
            if (filter.getTotalSalesMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalSales"), filter.getTotalSalesMin()));
            }
            if (filter.getTotalSalesMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalSales"), filter.getTotalSalesMax()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Sale> findByPeriod(LocalDate dateMin, LocalDate dateMax) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(dateMin != null || dateMax !=null){
                predicates.add(cb.between(root.get("dateSale"), dateMin, dateMax));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Sale> findByProductId(Product product){
        return (root, query, cb) -> {
            Join<Sale, ItemsSale> saleJoin = root.join("itemsSale");
            return cb.equal(saleJoin.get("product"), product);
        };
    }
}
