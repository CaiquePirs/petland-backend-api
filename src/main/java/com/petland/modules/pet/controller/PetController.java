package com.petland.modules.pet.controller;

import com.petland.common.auth.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.dto.PetUpdateDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;
    private final AccessValidator accessValidator;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<PetResponseDTO> create(@RequestBody @Valid PetRequestDTO petRequestDTO) {
        Pet pet = petService.create(petRequestDTO);
        return ResponseEntity.ok().body(petMapper.toDTO(pet));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<PetResponseDTO> findPetById(@PathVariable(name = "id") UUID petId){
        Pet pet = petService.findById(petId);
        accessValidator.isOwnerOrAdmin(pet.getOwner().getId());
        return ResponseEntity.ok().body(petMapper.toDTO(pet));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> deactivatePetById(@PathVariable(name = "id") UUID petId){
        petService.deactivateById(petId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PetResponseDTO>> listAllPetsByFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "breed", required = false) String breed,
            @RequestParam(value = "status", required = false, defaultValue = "ACTIVE") StatusEntity status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

      Page<PetResponseDTO> petsByFilters = petService.listAllByFilter(name, species, gender, breed, status, PageRequest.of(page, size));
      return ResponseEntity.ok().body(petsByFilters);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<PetResponseDTO> updatePetById(@RequestBody PetUpdateDTO petUpdateDTO,
                                                        @PathVariable(name = "id") UUID petId){
        Pet pet = petService.updateById(petId, petUpdateDTO);
        return ResponseEntity.ok(petMapper.toDTO(pet));
    }


}
