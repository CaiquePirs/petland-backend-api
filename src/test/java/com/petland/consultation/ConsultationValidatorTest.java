package com.petland.consultation;

import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.validator.ConsultationValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.controller.dtos.PetCareRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleRequestDTO;
import com.petland.modules.vaccination.controller.dto.VaccinationRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ConsultationValidatorTest {

    private ConsultationValidator consultationValidator;
    private Customer customer;
    private Pet pet;
    private String messageException;

    @BeforeEach
    void setUp(){
        consultationValidator = new ConsultationValidator();
        customer = Customer.builder().myPets(new ArrayList<>()).id(UUID.randomUUID()).build();
        pet = Pet.builder().owner(customer).id(UUID.randomUUID()).build();
        customer.getMyPets().add(pet);
        messageException = "The sale must be registered to the same customer as the one associated with the consultation.";
    }

    private ConsultationRequestDTO consultationRequest(SaleRequestDTO saleRequest,
                                                       VaccinationRequestDTO vaccinationRequest,
                                                       PetCareRequestDTO petCareRequest){
        return ConsultationRequestDTO.builder()
                .customerId(customer.getId())
                .petId(pet.getId())
                .saleRequestDTO(saleRequest)
                .vaccinationRequestDTO(vaccinationRequest)
                .petCareRequestDTO(petCareRequest)
                .build();
    }

    @Test
    void shouldValidateTheSameCustomerInRequisition(){
        SaleRequestDTO saleRequest = SaleRequestDTO.builder().customerId(customer.getId()).build();
        VaccinationRequestDTO vaccinationRequest = VaccinationRequestDTO.builder().customerId(customer.getId()).build();
        PetCareRequestDTO petCareRequest = PetCareRequestDTO.builder().customerId(customer.getId()).build();

        ConsultationRequestDTO consultationDTO = consultationRequest(saleRequest, vaccinationRequest, petCareRequest);

        assertDoesNotThrow(
                () -> consultationValidator.validateIfItIsTheSameCustomer(consultationDTO)
        );

        assertAll(
                () -> assertEquals(consultationDTO.petCareRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.vaccinationRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.saleRequestDTO().customerId(), customer.getId())
        );
    }

    @Test
    @DisplayName("Should throw exception if the customer ID in the sales request is not the same as the one entered in the query")
    void shouldThrowExceptionIfTheCustomerIdInSaleRequestNotSame() {
        SaleRequestDTO saleRequest = SaleRequestDTO.builder().customerId(UUID.randomUUID()).build();
        VaccinationRequestDTO vaccinationRequest = VaccinationRequestDTO.builder().customerId(customer.getId()).build();
        PetCareRequestDTO petCareRequest = PetCareRequestDTO.builder().customerId(customer.getId()).build();

        ConsultationRequestDTO consultationDTO = consultationRequest(saleRequest, vaccinationRequest, petCareRequest);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> consultationValidator.validateIfItIsTheSameCustomer(consultationDTO)
        );
        assertEquals(messageException, ex.getMessage());

        assertAll(
                () -> assertNotEquals(consultationDTO.saleRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.petCareRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.vaccinationRequestDTO().customerId(), customer.getId())
        );
    }

    @Test
    @DisplayName("Should throw exception if the customer ID in the vaccination request is not the same as the one entered in the query")
    void shouldThrowExceptionIfTheCustomerIdInVaccinationRequestNotSame() {
        SaleRequestDTO saleRequest = SaleRequestDTO.builder().customerId(customer.getId()).build();
        VaccinationRequestDTO vaccinationRequest = VaccinationRequestDTO.builder().customerId(UUID.randomUUID()).build();
        PetCareRequestDTO petCareRequest = PetCareRequestDTO.builder().customerId(customer.getId()).build();

        ConsultationRequestDTO consultationDTO = consultationRequest(saleRequest, vaccinationRequest, petCareRequest);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> consultationValidator.validateIfItIsTheSameCustomer(consultationDTO)
        );
        assertEquals(messageException, ex.getMessage());

        assertAll(
                () -> assertEquals(consultationDTO.saleRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.petCareRequestDTO().customerId(), customer.getId()),
                () -> assertNotEquals(consultationDTO.vaccinationRequestDTO().customerId(), customer.getId())
        );
    }

    @Test
    @DisplayName("Should throw exception if the customer ID in the service request is not the same as the one entered in the query")
    void shouldThrowExceptionIfTheCustomerIdInServiceRequestNotSame() {
        SaleRequestDTO saleRequest = SaleRequestDTO.builder().customerId(customer.getId()).build();
        VaccinationRequestDTO vaccinationRequest = VaccinationRequestDTO.builder().customerId(customer.getId()).build();
        PetCareRequestDTO petCareRequest = PetCareRequestDTO.builder().customerId(UUID.randomUUID()).build();

        ConsultationRequestDTO consultationDTO = consultationRequest(saleRequest, vaccinationRequest, petCareRequest);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> consultationValidator.validateIfItIsTheSameCustomer(consultationDTO)
        );
        assertEquals(messageException, ex.getMessage());

        assertAll(
                () -> assertEquals(consultationDTO.saleRequestDTO().customerId(), customer.getId()),
                () -> assertNotEquals(consultationDTO.petCareRequestDTO().customerId(), customer.getId()),
                () -> assertEquals(consultationDTO.vaccinationRequestDTO().customerId(), customer.getId())
        );
    }
}
