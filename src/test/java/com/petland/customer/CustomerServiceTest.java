package com.petland.customer;

import com.petland.common.auth.validator.EmailValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.EmailFoundException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.customer.builder.CustomerFilter;
import com.petland.modules.customer.controller.dto.CustomerRequestDTO;
import com.petland.modules.customer.controller.dto.CustomerResponseDTO;
import com.petland.modules.customer.controller.dto.UpdateCustomerDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.repository.CustomerRepository;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.customer.validator.CustomerUpdateValidator;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock CustomerRepository repository;
    @Mock EmailValidator emailValidator;
    @Mock CustomerMapper mapper;
    @Mock PasswordEncoder passwordEncoder;
    @Mock PetRepository petRepository;
    @Mock CustomerUpdateValidator updateValidator;
    @InjectMocks  private CustomerService customerService;

    private Customer customer;
    private CustomerRequestDTO customerDTO;
    private String encryptedPassword;

    @BeforeEach
    void setUp(){
        encryptedPassword = "$2a$12$ZIAZzdPEm23ctqneXRpXIusicQ5PeQKl8W72j/w5EUKcvahJLE3Y.";
        customer = Customer.builder().password(encryptedPassword).email("customer@gmail.com").build();
        customerDTO = CustomerRequestDTO.builder().password("customer@1234").email("customer@gmail.com").build();
    }

    @Test
    void shouldRegisterCustomerSuccessfully(){
        doNothing().when(emailValidator).checkIfEmailExists(customerDTO.email());
        when(passwordEncoder.encode(customerDTO.password())).thenReturn(encryptedPassword);
        when(mapper.toEntity(customerDTO)).thenReturn(customer);

        assertDoesNotThrow(() -> customerService.register(customerDTO));

        verify(mapper).toEntity(customerDTO);
        verify(passwordEncoder).encode(customerDTO.password());
        verify(repository).save(customer);
    }

    @Test
    void shouldThrowExceptionWhenEmailExist(){
        doThrow(new EmailFoundException("This email already exists"))
                .when(emailValidator).checkIfEmailExists(customerDTO.email());

        EmailFoundException ex = assertThrows(
                EmailFoundException.class,
                () -> customerService.register(customerDTO)
        );
        assertEquals("This email already exists", ex.getMessage());

        verify(emailValidator).checkIfEmailExists(customerDTO.email());
        verify(repository, never()).save(customer);
    }

    @Test
    void shouldFoundCustomerById(){
        UUID customerId = UUID.randomUUID();
        customer.setStatus(StatusEntity.ACTIVE);

        when(repository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer result = customerService.findById(customerId);

        assertEquals(customer, result);
        verify(repository).findById(customerId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when customer does not exist")
    void shouldThrowExceptionWhenCustomerNotFound() {
        UUID customerId = UUID.randomUUID();

        when(repository.findById(customerId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> customerService.findById(customerId)
        );
        assertEquals("Customer ID not found", ex.getMessage());
        verify(repository).findById(customerId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when customer is marked as DELETED")
    void shouldThrowExceptionWhenCustomerIsDeleted() {
        UUID customerId = UUID.randomUUID();
        Customer deletedCustomer = new Customer();
        deletedCustomer.setStatus(StatusEntity.DELETED);

        when(repository.findById(customerId)).thenReturn(Optional.of(deletedCustomer));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> customerService.findById(customerId)
        );
        assertEquals("Customer ID not found", ex.getMessage());
        verify(repository).findById(customerId);
    }


    @Test
    void shouldDeactivateCustomerAndPetsById() {
        UUID customerId = UUID.randomUUID();
        customer.setStatus(StatusEntity.ACTIVE);

        Pet pet1 = new Pet();
        pet1.setStatus(StatusEntity.ACTIVE);
        Pet pet2 = new Pet();
        pet2.setStatus(StatusEntity.ACTIVE);
        customer.setMyPets(List.of(pet1, pet2));

        when(repository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.deactivateById(customerId);

        assertEquals(StatusEntity.DELETED, customer.getStatus());
        assertEquals(StatusEntity.DELETED, pet1.getStatus());
        assertEquals(StatusEntity.DELETED, pet2.getStatus());

        verify(petRepository).saveAll(List.of(pet1, pet2));
        verify(repository).save(customer);
    }

    @Test
    void shouldListCustomersByFilter() {
        CustomerFilter filter = CustomerFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        CustomerResponseDTO dto = CustomerResponseDTO.builder().build();;

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);
        when(mapper.toDTO(customer)).thenReturn(dto);

        Page<CustomerResponseDTO> result = customerService.listAllByFilter(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));

        verify(repository).findAll(any(Specification.class), eq(pageable));
        verify(mapper).toDTO(customer);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        UUID customerId = UUID.randomUUID();
        UpdateCustomerDTO updateDTO = UpdateCustomerDTO.builder().build();

        Customer existingCustomer = new Customer();
        Customer validatedCustomer = new Customer();

        when(repository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(updateValidator.validate(existingCustomer, updateDTO)).thenReturn(validatedCustomer);
        when(repository.save(validatedCustomer)).thenReturn(validatedCustomer);

        Customer result = customerService.updateById(customerId, updateDTO);

        assertEquals(validatedCustomer, result);

        verify(repository).findById(customerId);
        verify(updateValidator).validate(existingCustomer, updateDTO);
        verify(repository).save(validatedCustomer);
    }



}
