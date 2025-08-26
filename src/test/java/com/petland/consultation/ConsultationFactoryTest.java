package com.petland.consultation;

import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.strategy.factory.ConsultationFactory;
import com.petland.modules.consultation.strategy.impl.PetCareConsultationImpl;
import com.petland.modules.consultation.strategy.impl.SalesConsultationImpl;
import com.petland.modules.consultation.strategy.impl.VaccinationConsultationImpl;
import com.petland.modules.petCare.dtos.PetCareRequestDTO;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.sale.dtos.SaleRequestDTO;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.module.Vaccination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsultationFactoryTest {

    @Mock private SalesConsultationImpl salesStrategy;
    @Mock private VaccinationConsultationImpl vaccinationStrategy;
    @Mock private PetCareConsultationImpl petCareStrategy;
    private ConsultationFactory factory;

    private SaleRequestDTO saleRequestDTO;
    private VaccinationRequestDTO vaccinationRequestDTO;
    private PetCareRequestDTO petCareRequestDTO;

    private Sale sale;
    private Vaccination vaccination;
    private PetCare petCare;

    @BeforeEach
    void setUp() {
        saleRequestDTO = SaleRequestDTO.builder().build();
        vaccinationRequestDTO = VaccinationRequestDTO.builder().build();
        petCareRequestDTO = PetCareRequestDTO.builder().build();

        sale = new Sale();
        vaccination = new Vaccination();
        petCare = new PetCare();

        factory = new ConsultationFactory(List.of(salesStrategy, vaccinationStrategy, petCareStrategy));
    }

    private Consultation consultation(){
        return Consultation.builder().build();
    }

    private ConsultationRequestDTO requestDTO(SaleRequestDTO saleDTO, VaccinationRequestDTO vaccinationDTO, PetCareRequestDTO petCareDTO){
        return ConsultationRequestDTO.builder()
                .saleRequestDTO(saleDTO)
                .vaccinationRequestDTO(vaccinationDTO)
                .petCareRequestDTO(petCareDTO)
                .build();
    }

    @Test
    @DisplayName("Should create Sales, Vaccination and Petcare records when consultation is registered")
    void shouldCreateAllServicesSuccessfully() {
        ConsultationRequestDTO dto = requestDTO(saleRequestDTO, vaccinationRequestDTO, petCareRequestDTO);
        Consultation consultationInitial = consultation();

        Consultation afterSales = consultation();
        afterSales.setSales(sale);

        Consultation afterVaccination = consultation();
        afterVaccination.setSales(sale);
        afterVaccination.setVaccination(vaccination);

        Consultation afterPetCare = consultation();
        afterPetCare.setSales(sale);
        afterPetCare.setVaccination(vaccination);
        afterPetCare.setService(petCare);

        when(salesStrategy.execute(consultationInitial, dto)).thenReturn(afterSales);
        when(vaccinationStrategy.execute(afterSales, dto)).thenReturn(afterVaccination);
        when(petCareStrategy.execute(afterVaccination, dto)).thenReturn(afterPetCare);

        Consultation result = factory.execute(consultationInitial, dto);

        assertNotNull(result.getSales());
        assertNotNull(result.getVaccination());
        assertNotNull(result.getService());

        verify(salesStrategy).execute(consultationInitial, dto);
        verify(vaccinationStrategy).execute(afterSales, dto);
        verify(petCareStrategy).execute(afterVaccination, dto);
    }

    @Test
    @DisplayName("Should skip SaleStrategy when saleRequest is null and execute others")
    void shouldSkipSaleStrategyWhenSaleRequestIsNull() {
        ConsultationRequestDTO dto = requestDTO(null, vaccinationRequestDTO, petCareRequestDTO);
        Consultation consultationInitial = consultation();

        Consultation afterVaccination = consultation();
        afterVaccination.setVaccination(vaccination);

        Consultation afterPetCare = consultation();
        afterPetCare.setVaccination(vaccination);
        afterPetCare.setService(petCare);

        when(salesStrategy.execute(consultationInitial, dto)).thenReturn(consultationInitial);
        when(vaccinationStrategy.execute(consultationInitial, dto)).thenReturn(afterVaccination);
        when(petCareStrategy.execute(afterVaccination, dto)).thenReturn(afterPetCare);

        Consultation result = factory.execute(consultationInitial, dto);

        assertNull(result.getSales());
        assertNotNull(result.getVaccination());
        assertNotNull(result.getService());

        verify(vaccinationStrategy).execute(consultationInitial, dto);
        verify(petCareStrategy).execute(afterVaccination, dto);
    }

    @Test
    @DisplayName("Should skip VaccinationStrategy when vaccinationRequest is null and execute others")
    void shouldSkipSaleStrategyWhenVaccinationRequestIsNull() {
        ConsultationRequestDTO dto = requestDTO(saleRequestDTO, null, petCareRequestDTO);
        Consultation consultationInitial = consultation();

        Consultation afterSales = consultation();
        afterSales.setSales(sale);

        Consultation afterPetCare = consultation();
        afterPetCare.setSales(sale);
        afterPetCare.setService(petCare);

        when(salesStrategy.execute(consultationInitial, dto)).thenReturn(afterSales);
        when(vaccinationStrategy.execute(afterSales, dto)).thenReturn(afterSales);
        when(petCareStrategy.execute(afterSales, dto)).thenReturn(afterPetCare);

        Consultation result = factory.execute(consultationInitial, dto);

        assertNotNull(result.getSales());
        assertNull(result.getVaccination());
        assertNotNull(result.getService());

        verify(salesStrategy).execute(consultationInitial, dto);
        verify(petCareStrategy).execute(afterSales, dto);
    }

    @Test
    @DisplayName("Should skip PetCare Strategy when petcareRequest is null and execute others")
    void shouldSkipPetCareStrategyWhenPetCareRequestIsNull() {
        ConsultationRequestDTO dto = requestDTO(saleRequestDTO, vaccinationRequestDTO, null);
        Consultation consultationInitial = consultation();

        Consultation afterSales = consultation();
        afterSales.setSales(sale);

        Consultation afterVaccination = consultation();
        afterVaccination.setSales(sale);
        afterVaccination.setVaccination(vaccination);

        when(salesStrategy.execute(consultationInitial, dto)).thenReturn(afterSales);
        when(vaccinationStrategy.execute(afterSales, dto)).thenReturn(afterVaccination);
        when(petCareStrategy.execute(afterVaccination, dto)).thenReturn(afterVaccination);

        Consultation result = factory.execute(consultationInitial, dto);

        assertNotNull(result.getSales());
        assertNotNull(result.getVaccination());
        assertNull(result.getService());

        verify(salesStrategy).execute(consultationInitial, dto);
        verify(vaccinationStrategy).execute(afterSales, dto);
    }

    @Test
    @DisplayName("Should skip strategies when no service is informed")
    void shouldSkipStrategiesWhenNoServiceIsInformed() {
        ConsultationRequestDTO dto = requestDTO(null, null, null);
        Consultation consultationInitial = consultation();

        when(salesStrategy.execute(consultationInitial, dto)).thenReturn(consultationInitial);
        when(vaccinationStrategy.execute(consultationInitial, dto)).thenReturn(consultationInitial);
        when(petCareStrategy.execute(consultationInitial, dto)).thenReturn(consultationInitial);

        Consultation result = factory.execute(consultationInitial, dto);

        assertNull(result.getSales());
        assertNull(result.getVaccination());
        assertNull(result.getService());
    }

}
