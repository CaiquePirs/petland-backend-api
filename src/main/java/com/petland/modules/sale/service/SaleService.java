package com.petland.modules.sale.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final ItemsSaleService itemsSaleService;
    private final AccessValidator accessValidator;
    private final SaleRepository saleRepository;
    private final SaleCalculator calculator;

    @Transactional
    public Sale registerSale(SaleRequestDTO saleRequestDTO){
        UUID employeeId = accessValidator.getLoggedInUser();

        Employee employee = employeeService.findById(employeeId);
        Customer customer = customerService.findCustomerById(saleRequestDTO.customerId());

        List<ItemsSale> listItemsSale = itemsSaleService.createItems(sale, saleRequestDTO.itemsSaleRequestDTO());
        BigDecimal totalSale = calculator.calculateTotalItemsSale(listItemsSale);

        BigDecimal totalSale = BigDecimal.ZERO;

        for(ItemsSale itemsSale : listItemsSale){
            totalSale = totalSale.add(itemsSale.getItemsSaleTotal());
        }

        sale.setEmployee(employee);
        sale.setCustomer(customer);
        sale.setPaymentType(saleRequestDTO.paymentType());
        sale.setStatus(StatusEntity.ACTIVE);
        sale.setItemsSale(listItemsSale);
        sale.setTotalSales(totalSale);

        return saleRepository.save(sale);
    }

    public Sale findSaleById(UUID saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale ID not found"));

        if(sale.getStatus().equals(StatusEntity.DELETED)){
              throw new NotFoundException("Sale ID not found");
        }
        return sale;
    }

    @Transactional
    public void deleteSaleById(UUID saleId){
        Sale sale = findSaleById(saleId);
        itemsSaleService.deleteItemsList(sale.getItemsSale());
        sale.setStatus(StatusEntity.DELETED);
        saleRepository.save(sale);
    }


}
