package com.petland.vaccination;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InsufficientStockException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.vaccination.builder.VaccineFilter;
import com.petland.modules.vaccination.dto.VaccineRequestDTO;
import com.petland.modules.vaccination.dto.VaccineResponseDTO;
import com.petland.modules.vaccination.dto.VaccineUpdateDTO;
import com.petland.modules.vaccination.mappers.VaccineMapper;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.VaccineRepository;
import com.petland.modules.vaccination.service.VaccineService;
import com.petland.modules.vaccination.validator.VaccineUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaccineServiceTest {

    @Mock private VaccineMapper mapper;
    @Mock private VaccineRepository vaccineRepository;
    @Mock private VaccineUpdateValidator validator;
    @InjectMocks private VaccineService service;

    private UUID vaccineId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        vaccineId = UUID.randomUUID();
    }

    @Test
    void shouldCreateVaccine() {
        VaccineRequestDTO dto = mock(VaccineRequestDTO.class);
        Vaccine vaccine = new Vaccine();

        when(mapper.toEntity(dto)).thenReturn(vaccine);
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);

        Vaccine result = service.create(dto);

        assertThat(result).isEqualTo(vaccine);
        verify(mapper).toEntity(dto);
        verify(vaccineRepository).save(vaccine);
    }

    @Test
    void shouldFindVaccineById() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));

        Vaccine result = service.findById(vaccineId);

        assertThat(result).isEqualTo(vaccine);
    }

    @Test
    void shouldThrowExceptionWhenVaccineNotFound() {
        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(vaccineId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Vaccine ID not found");
    }

    @Test
    void shouldThrowExceptionWhenVaccineIsDeleted() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.DELETED);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));

        assertThatThrownBy(() -> service.findById(vaccineId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Vaccine ID not found");
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);
        vaccine.setStockQuantity(10);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));
        when(vaccineRepository.save(any(Vaccine.class))).thenReturn(vaccine);

        Vaccine result = service.updateStock(3, vaccineId);

        assertThat(result.getStockQuantity()).isEqualTo(7);
        verify(vaccineRepository).save(vaccine);
    }

    @Test
    void shouldThrowExceptionWhenStockIsZero() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);
        vaccine.setStockQuantity(0);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));

        assertThatThrownBy(() -> service.updateStock(1, vaccineId))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("Vaccine stock is insufficient");
    }

    @Test
    void shouldThrowExceptionWhenStockUsedGreaterThanAvailable() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);
        vaccine.setStockQuantity(5);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));

        assertThatThrownBy(() -> service.updateStock(10, vaccineId))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("Vaccine stock is insufficient");
    }

    @Test
    void shouldDeactivateVaccine() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));

        service.deactivateById(vaccineId);

        assertThat(vaccine.getStatus()).isEqualTo(StatusEntity.DELETED);
        verify(vaccineRepository).save(vaccine);
    }

    @Test
    void shouldFilterVaccines() {
        VaccineFilter filter = VaccineFilter.builder().build();
        Pageable pageable = mock(Pageable.class);

        Vaccine vaccine = new Vaccine();
        VaccineResponseDTO dto = VaccineResponseDTO.builder().build();

        Page<Vaccine> page = new PageImpl<>(List.of(vaccine));

        when(vaccineRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toDTO(vaccine)).thenReturn(dto);

        Page<VaccineResponseDTO> result = service.filterAllVaccinesByFilter(filter, pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(vaccineRepository).findAll(any(Specification.class), eq(pageable));
        verify(mapper).toDTO(vaccine);
    }

    @Test
    void shouldUpdateByIdSuccessfully() {
        Vaccine vaccine = new Vaccine();
        vaccine.setStatus(StatusEntity.ACTIVE);

        VaccineUpdateDTO dto = VaccineUpdateDTO.builder().build();

        when(vaccineRepository.findById(vaccineId)).thenReturn(Optional.of(vaccine));
        when(validator.validate(vaccine, dto)).thenReturn(vaccine);
        when(vaccineRepository.save(vaccine)).thenReturn(vaccine);

        Vaccine result = service.updateById(vaccineId, dto);

        assertThat(result).isEqualTo(vaccine);
        verify(validator).validate(vaccine, dto);
        verify(vaccineRepository).save(vaccine);
    }
}

