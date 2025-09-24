package com.petland.consultation;

import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.petCare.controller.dtos.PetCareRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleRequestDTO;
import com.petland.modules.vaccination.controller.dto.VaccinationRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultationRequestTest {

     private SaleRequestDTO saleRequestDTO;
     private VaccinationRequestDTO vaccinationRequestDTO;
     private PetCareRequestDTO petCareRequestDTO;
     private Validator validator;

    @BeforeEach
    void setUp(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        saleRequestDTO = SaleRequestDTO.builder().customerId(UUID.randomUUID()).build();
        vaccinationRequestDTO = VaccinationRequestDTO.builder().customerId(UUID.randomUUID()).build();
        petCareRequestDTO = PetCareRequestDTO.builder().customerId(UUID.randomUUID()).build();
    }

    static Stream<Arguments> generateArguments(){
        ConsultationDetails details = new ConsultationDetails();
        return Stream.of(
                Arguments.of(null, UUID.randomUUID(), details, "Customer ID is required"),
                Arguments.of(UUID.randomUUID(), null, details, "Pet ID is required"),
                Arguments.of(UUID.randomUUID(), UUID.randomUUID(), null, "Service details is required")
        );
    }

    @ParameterizedTest(name = "Test: {index} - Expected= {3}")
    @MethodSource("generateArguments")
    void shouldValidateInvalidArguments(UUID customerId, UUID petId, ConsultationDetails details, String messageExpected){
        ConsultationRequestDTO dto = new ConsultationRequestDTO(
                customerId, petId, details,
                saleRequestDTO, vaccinationRequestDTO, petCareRequestDTO
        );

        Set<ConstraintViolation<ConsultationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals(messageExpected))
        );
    }

    @Test
    void shouldAcceptRequestWithValidValues(){
        UUID customerId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        ConsultationDetails details = new ConsultationDetails();

        ConsultationRequestDTO dto = new ConsultationRequestDTO(
                customerId, petId, details,
                saleRequestDTO, vaccinationRequestDTO, petCareRequestDTO
        );

        validator.validate(dto);
        assertAll(
                () -> assertEquals(dto.customerId(), customerId),
                () -> assertEquals(dto.petId(), petId),
                () -> assertEquals(dto.details(), details),
                () -> assertEquals(dto.saleRequestDTO().customerId(), saleRequestDTO.customerId()),
                () -> assertEquals(dto.vaccinationRequestDTO().customerId(), vaccinationRequestDTO.customerId()),
                () -> assertEquals(dto.petCareRequestDTO().customerId(), petCareRequestDTO.customerId())
        );
    }

}
