package com.petland.petCare;

import com.petland.common.entity.Address;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.controller.dtos.PetCareDetailsHistoryResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareDetailsResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.controller.dtos.PetCareResponseDTO;
import com.petland.modules.petCare.model.enums.PetCareType;
import com.petland.modules.petCare.mappers.PetCareDetailsMapper;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.mappers.PetCareMapperGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PetCareMapperGeneratorTest {

    private PetCareDetailsMapper mapper;
    private PetCareMapperGenerator generate;

    private Pet pet;
    private Customer customer;
    private Employee employee;
    private Address address;

    @BeforeEach
    void setUp(){
        pet = Pet.builder().id(UUID.randomUUID()).build();
        customer = Customer.builder().id(UUID.randomUUID()).build();
        employee = Employee.builder().id(UUID.randomUUID()).build();
        address = Address.builder().country("Brazil").city("Salvador").build();

        mapper = Mappers.getMapper(PetCareDetailsMapper.class);
        generate = new PetCareMapperGenerator(mapper);
    }

    private PetCareDetails petCareDetails(){
        return PetCareDetails.builder()
                .id(UUID.randomUUID())
                .petCareType(PetCareType.BATH)
                .unitPrice(BigDecimal.valueOf(20))
                .quantityService(5)
                .noteService("Details Test")
                .totalByService(BigDecimal.valueOf(100))
                .profitByService(BigDecimal.valueOf(50))
                .operatingCost(BigDecimal.valueOf(50))
                .petCare(PetCare.builder().id(UUID.randomUUID()).build())
                .build();
    }

    private PetCare petCare(){
        PetCareDetails details1 = petCareDetails();
        PetCareDetails details2 = petCareDetails();
        return PetCare.builder()
                .id(UUID.randomUUID())
                .pet(pet)
                .customer(customer)
                .employee(employee)
                .serviceDate(LocalDateTime.now())
                .totalRevenue(BigDecimal.valueOf(300))
                .totalProfit(BigDecimal.valueOf(150))
                .totalCostOperating(BigDecimal.valueOf(150))
                .location(address)
                .petCareDetails(List.of(details1, details2))
                .build();
    }

    private List<PetCare> petCares(){
        return List.of(petCare(), petCare(), petCare());
    }

    @Test
    void shouldGeneratePetCareResponseSuccessfully(){
        PetCare petCare = petCare();
        PetCareResponseDTO petCareResponse = generate.generate(petCare);

        assertNotNull(petCareResponse);
        assertAll(
                () -> assertEquals(petCare.getId(), petCareResponse.id()),
                () -> assertEquals(petCare.getPet().getId(), petCareResponse.petId()),
                () -> assertEquals(petCare.getCustomer().getId(), petCareResponse.customerId()),
                () -> assertEquals(petCare.getEmployee().getId(), petCareResponse.employeeId()),
                () -> assertEquals(petCare.getTotalRevenue(), petCareResponse.totalRevenue()),
                () -> assertEquals(petCare.getTotalProfit(), petCareResponse.totalProfit()),
                () -> assertEquals(petCare.getTotalCostOperating(), petCareResponse.totalCostOperating())
        );
    }

    @Test
    void shouldGeneratePetCareDetailsResponseSuccessfully(){
        PetCare petCare = petCare();
        List<PetCareDetailsResponseDTO> detailsResponse = generate.generatePetCareDetailsList(
                petCare.getPetCareDetails()
        );

        assertNotNull(detailsResponse);
        assertEquals(2, detailsResponse.size());
        assertAll(
                () -> assertEquals(petCare.getPetCareDetails().get(0).getId(), detailsResponse.get(0).id()),
                () -> assertEquals(petCare.getPetCareDetails().get(1).getId(), detailsResponse.get(1).id())
        );
    }

    @Test
    void shouldMapServiceDetailsSuccessfully(){
        List<PetCareDetails> details = petCare().getPetCareDetails();
        List<PetCareDetailsHistoryResponseDTO> detailsResult = generate.mapServiceDetails(details);

        assertNotNull(detailsResult);
        assertEquals(2, detailsResult.size());
        assertAll(
                () -> assertEquals(details.get(0).getId(), detailsResult.get(0).id()),
                () -> assertEquals(details.get(1).getId(),detailsResult.get(1).id())
        );
    }

    @Test
    void shouldMapToPetCareHistoryResponseDTO(){
        PetCare petCare = petCare();
        PetCareHistoryResponseDTO result = generate.mapToCustomerServiceHistory(petCare);

        assertNotNull(result);
        assertAll(
                () -> assertEquals(petCare.getId(), result.id()),
                () -> assertEquals(petCare.getPet().getId(), result.petId()),
                () -> assertEquals(petCare.getCustomer().getId(), result.customerId()),
                () -> assertEquals(petCare.getEmployee().getId(), result.employeeId()),
                () -> assertEquals(petCare.getTotalRevenue(), result.totalService())
        );

    }


}
