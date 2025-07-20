package com.petland.modules.sale.service;

import com.petland.common.auth.AccessValidator;
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

    @Transactional
    public Sale registerSale(SaleRequestDTO saleRequestDTO){
        UUID employeeId = accessValidator.getLoggedInUser();

        Employee employee = employeeService.findById(employeeId);
        Customer customer = customerService.findCustomerById(saleRequestDTO.customerId());

        Sale sale = new Sale();

        List<ItemsSale> listItemsSale = itemsSaleService.createItemsSale(sale, saleRequestDTO.itemsSaleRequestDTO());

        BigDecimal totalSale = BigDecimal.ZERO;

        for(ItemsSale itemsSale : listItemsSale){
            totalSale = totalSale.add(itemsSale.getItemsSaleTotal());
        }

        sale.setEmployee(employee);
        sale.setCustomer(customer);
        sale.setEmployeeAudit(employeeId);
        sale.setPaymentType(saleRequestDTO.paymentType());
        sale.setStatusEntity(StatusEntity.ACTIVE);
        sale.setItemsSale(listItemsSale);
        sale.setTotalSales(totalSale);

        return saleRepository.save(sale);
    }
}
