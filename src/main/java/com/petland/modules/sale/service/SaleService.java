package com.petland.modules.sale.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.builder.SaleFilter;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.sale.specifications.SaleSpecifications;
import com.petland.modules.sale.util.GenerateSaleResponse;
import com.petland.modules.sale.calculator.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final ItemsSaleService itemsSaleService;
    private final AccessValidator access;
    private final SaleRepository repository;
    private final SaleCalculator calculator;
    private final GenerateSaleResponse generate;

    @Transactional
    public Sale registerSale(SaleRequestDTO dto){
        Customer customer = customerService.findById(dto.customerId());
        List<ItemsSale> itemsSale = itemsSaleService.createItems(dto.itemsSaleRequestDTO());
        BigDecimal totalSale = calculator.calculateTotalBilledByItemsSale(itemsSale);
        BigDecimal profitSale = calculator.calculateProfitByItemsSale(itemsSale);

        Sale sale = Sale.builder()
                .itemsSale(itemsSale)
                .totalSales(totalSale)
                .profitSale(profitSale)
                .employee(access.getEmployeeLogged())
                .customer(customer)
                .paymentType(dto.paymentType())
                .build();

        itemsSale.forEach(i -> i.setSale(sale));
        customer.getSalesHistory().add(sale);
        return repository.save(sale);
    }

    public Sale findSaleById(UUID saleId){
     return repository.findById(saleId)
             .filter(s -> !s.getStatus().equals(StatusEntity.DELETED))
             .orElseThrow(() -> new NotFoundException("Sale not found"));
    }

    @Transactional
    public void deactivateSaleById(UUID saleId){
        Sale sale = findSaleById(saleId);
        itemsSaleService.deactivateItemsList(sale.getItemsSale());
        sale.setStatus(StatusEntity.DELETED);
        repository.save(sale);
    }

    public Page<SaleResponseDTO> findSalesByCustomerId(UUID customerId, Pageable pageable) {
        return repository.findByCustomerId(customerService.findById(customerId).getId(), pageable)
                .map(generate::generateSaleResponse);
    }


    public Page<SaleResponseDTO> findAllSalesByFilter(SaleFilter filter, Pageable pageable){
        return repository.findAll(SaleSpecifications.specifications(filter), pageable)
                .map(generate::generateSaleResponse);
    }

    public List<Sale> findAllSalesByPeriod(LocalDate dateMin, LocalDate dateMax){
        return repository.findAll(SaleSpecifications.findByPeriod(dateMin, dateMax))
                .stream()
                .filter(s -> !s.getStatus().equals(StatusEntity.DELETED)).toList();
    }

    public List<Sale> findAllSalesByProductId(UUID productId){
        return repository.findAll(SaleSpecifications.findByProductId(productService.findById(productId)))
                .stream()
                .filter(s -> !s.getStatus().equals(StatusEntity.DELETED)).toList();
    }
}
