package com.petland.modules.pet.controller;

import com.petland.modules.pet.dto.PetRequestDTO;
import com.petland.modules.pet.dto.PetResponseDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final HttpServletRequest request;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'RECEPTIONIST')")
    public ResponseEntity<PetResponseDTO> create(@RequestBody @Valid PetRequestDTO petRequestDTO) {
        Pet pet = petService.create(petRequestDTO);
        return ResponseEntity.ok().body(petMapper.toDTO(pet));
    }

    @GetMapping("{petId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PetResponseDTO> findPetById(@PathVariable UUID petId){
        String customerId = request.getAttribute("id").toString();
        Pet pet = petService.findPetById(petId, UUID.fromString(customerId));
        return ResponseEntity.ok().body(petMapper.toDTO(pet));
    }

}
