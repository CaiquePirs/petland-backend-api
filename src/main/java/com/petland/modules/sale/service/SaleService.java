package com.petland.modules.sale.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.attendance.enums.PaymentType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.sale.specifications.SaleSpecifications;
import com.petland.modules.sale.util.GenerateSaleResponse;
import com.petland.modules.sale.util.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final EmployeeService employeeService;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final ItemsSaleService itemsSaleService;
    private final AccessValidator accessValidator;
    private final SaleRepository saleRepository;
    private final SaleCalculator calculator;
    private final GenerateSaleResponse generateSaleResponse;

    @Transactional
    public Sale registerSale(SaleRequestDTO saleRequestDTO){
        Sale sale = new Sale();
        UUID employeeId = accessValidator.getLoggedInUser();
        Employee employee = employeeService.findById(employeeId);
        Customer customer = customerService.findCustomerById(saleRequestDTO.customerId());

        List<ItemsSale> listItemsSale = itemsSaleService.createItems(sale, saleRequestDTO.itemsSaleRequestDTO());
        BigDecimal totalSale = calculator.calculateTotalItemsSale(listItemsSale);
        BigDecimal profitSale = calculator.calculateProfitByItemsSale(listItemsSale);

        sale.setItemsSale(listItemsSale);
        sale.setEmployee(employee);
        sale.setTotalSales(totalSale);
        sale.setCustomer(customer);
        sale.setPaymentType(saleRequestDTO.paymentType());
        sale.setProfitSale(profitSale);
        sale.setCreateAt(LocalDateTime.now());

        customer.getSalesHistory().add(sale);
        customerRepository.save(customer);
        return saleRepository.save(sale);
    }

    public Sale findSaleById(UUID saleId){
     return saleRepository.findById(saleId)
                .filter(s -> !s.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Sale not found"));
    }

    @Transactional
    public void deleteSaleById(UUID saleId){
        Sale sale = findSaleById(saleId);
        itemsSaleService.deleteItemsList(sale.getItemsSale());
        sale.setStatus(StatusEntity.DELETED);
        saleRepository.save(sale);
    }

    public Page<SaleResponseDTO> findSalesByCustomerId(UUID customerId, Pageable pageable) {
        Customer customer = customerService.findCustomerById(customerId);
        Page<Sale> listSales = saleRepository.findByCustomerId(customer.getId(), pageable);

        if(listSales.isEmpty()){
            throw new NotFoundException("Sales by customer ID not found");
        }

        List<SaleResponseDTO> listSaleResponse = generateSaleResponse.generateListSaleResponse(
                listSales.getContent()
        );
        return new PageImpl<>(listSaleResponse, pageable, listSales.getSize());
    }


    public Page<SaleResponseDTO> findAllSalesByFilter(UUID employeeId, UUID customerId, PaymentType paymentType, BigDecimal totalSalesMin,
                                                      BigDecimal totalSalesMax, StatusEntity status , Pageable pageable){
        List<SaleResponseDTO> salesList = saleRepository.findAll(SaleSpecifications.specifications(
                employeeId, customerId, paymentType, totalSalesMin, totalSalesMax, status), pageable)
                .stream()
                .map(generateSaleResponse::generateSaleResponse)
                .toList();
        return new PageImpl<>(salesList, pageable, salesList.size());
    }

    public List<Sale> findAllSalesByPeriod(LocalDate dateMin, LocalDate dateMax){
        return saleRepository.findAll(SaleSpecifications.findByPeriod(dateMin, dateMax));
    }

}
