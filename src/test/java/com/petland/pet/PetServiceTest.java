package com.petland.pet;


import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.builder.PetFilter;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.dto.PetUpdateDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.repository.PetRepository;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock private CustomerService customerService;
    @Mock private PetRepository petRepository;
    @Mock private PetMapper petMapper;
    @Mock private AccessValidator accessValidator;
    @Mock private PetUpdateValidator petUpdateValidator;
    @InjectMocks private PetService petService;

    private UUID customerId;
    private UUID petId;
    private Customer customer;
    private Pet pet;
    private PetRequestDTO petRequest;

    @BeforeEach
    void setUp(){
        customerId = UUID.randomUUID();
        petId = UUID.randomUUID();
        petRequest = PetRequestDTO.builder().customerId(customerId).build();

        customer = new Customer();
        customer.getMyPets().add(pet);
        customer.setId(customerId);

        pet = new Pet();
        pet.setId(UUID.randomUUID());
    }

    @Test
    void shouldCreatePetSuccessfully(){
        when(customerService.findById(petRequest.customerId())).thenReturn(customer);
        when(petMapper.toEntity(petRequest)).thenReturn(pet);
        when(petRepository.save(pet)).thenReturn(pet);

        Pet result = petService.create(petRequest);

        assertEquals(petRequest.customerId(), result.getOwner().getId());

        verify(customerService).findById(customerId);
        verify(petMapper).toEntity(petRequest);
        verify(petRepository).save(pet);
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound() {
        doThrow(new NotFoundException("Customer ID not found"))
                .when(customerService).findById(petRequest.customerId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> petService.create(petRequest)
        );
        assertEquals("Customer ID not found", ex.getMessage());

        verify(customerService).findById(petRequest.customerId());
        verify(petRepository, never()).save(pet);
    }

    @Test
    void shouldReturnPetWhenFoundById(){
        pet.setStatus(StatusEntity.ACTIVE);
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        petService.findById(petId);

        verify(petRepository).findById(petId);
    }

    @Test
    void shouldThrowExceptionWhenPetIsNotFoundBy(){
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                ()-> petService.findById(petId)
        );
        assertEquals("Pet ID not found", ex.getMessage());
        verify(petRepository).findById(petId);
    }

    @Test
    void shouldThrowExceptionWhenPetIsWithStatusDeleted(){
        pet.setStatus(StatusEntity.DELETED);
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        NotFoundException ex = assertThrows(NotFoundException.class,
                ()-> petService.findById(petId)
        );
        assertEquals("Pet ID not found", ex.getMessage());
        verify(petRepository).findById(petId);
    }

    @Test
    void shouldDeactivateByIdSuccessfully() {
        pet.setOwner(customer);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        doNothing().when(accessValidator).isOwnerOrAdmin(customerId);

        petService.deactivateById(petId);

        assertEquals(StatusEntity.DELETED, pet.getStatus());
        verify(petRepository, times(1)).save(pet);
    }


    @Test
    void shouldThrowExceptionWhenDeactivationFailsDueToOwnership() {
        pet.setOwner(customer);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        doThrow(new UnauthorizedException("User not authorized"))
                .when(accessValidator).isOwnerOrAdmin(pet.getOwner().getId());

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> petService.deactivateById(petId)
        );

        assertEquals("User not authorized", ex.getMessage());
        verify(petRepository, never()).save(pet);
    }

    @Test
    public void shouldReturnPetsWhenCustomerExists() {
        Pet pet = new Pet(); pet.setStatus(StatusEntity.ACTIVE);
        when(customerService.findById(customerId)).thenReturn(new Customer());
        when(petRepository.findPetByOwnerId(customerId)).thenReturn(List.of(pet));

        List<Pet> result = petService.getPetsByCustomerId(customerId);

        assertEquals(1, result.size());
        verify(customerService).findById(customerId);
        verify(petRepository).findPetByOwnerId(customerId);
    }


    @Test
    public void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerService.findById(customerId)).thenThrow(new NotFoundException("Customer ID not found"));

        assertThrows(NotFoundException.class, () -> petService.getPetsByCustomerId(customerId));

        verify(customerService).findById(customerId);
        verifyNoInteractions(petRepository);
    }

    @Test
    public void shouldReturnEmptyPageWhenFilterDoesNotMatchAnyPet() {
        PetFilter filter = PetFilter.builder().build();
        Pageable pageable = mock(Pageable.class);
        Page<Pet> emptyPage = new PageImpl<>(List.of());

        when(petRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<PetResponseDTO> result = petService.listAllByFilter(filter, pageable);

        assertTrue(result.isEmpty());
        verify(petRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void shouldUpdatePetWhenValidOwnerAndData() {
        pet.setOwner(customer);
        PetUpdateDTO updateDTO = PetUpdateDTO.builder().build();

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        doNothing().when(accessValidator).isOwnerOrAdmin(pet.getOwner().getId());

        when(petUpdateValidator.validate(updateDTO, pet)).thenReturn(pet);
        when(petRepository.save(pet)).thenReturn(pet);

        Pet result = petService.updateById(petId, updateDTO);

        assertEquals(pet, result);
        verify(accessValidator).isOwnerOrAdmin(pet.getOwner().getId());
        verify(petRepository).save(pet);
    }


}
