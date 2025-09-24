package com.petland.modules.pet.controller.doc;

import com.petland.common.error.ErrorResponseDTO;
import com.petland.modules.pet.builder.PetFilter;
import com.petland.modules.pet.controller.dto.PetRequestDTO;
import com.petland.modules.pet.controller.dto.PetResponseDTO;
import com.petland.modules.pet.controller.dto.PetUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Pet", description = "Endpoints for managing pets")
public interface PetApi {

    @Operation(summary = "Pet Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet registered successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Customer ID not found" }
                      ]
                    }
                """))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 400,
                      "message": "Validation failed",
                      "errors": [
                        { "field": "name", "message": "Name is required" },
                        { "field": "age", "message": "Age is required" },
                        { "field": "dateBirth", "message": "Date birth is required" },
                        { "field": "specie", "message": "Specie is required" },
                        { "field": "gender", "message": "Gender is required" },
                        { "field": "breed", "message": "Breed is required" },
                        { "field": "weight", "message": "Weight is required" },
                        { "field": "customerId", "message": "Customer ID is required" }
                      ]
                    }
                """)))
    })
    ResponseEntity<PetResponseDTO> create(@RequestBody @Valid PetRequestDTO petRequestDTO);


    @Operation(summary = "Find Pet By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet found successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Pet ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<PetResponseDTO> findPetById(@PathVariable(name = "id") UUID petId);

    @Operation(summary = "Deactivate Pet By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet deactivate successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Pet ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<Void> deactivatePetById(@PathVariable(name = "id") UUID petId);

    @Operation(summary = "List All Pet By Filter")
    @ApiResponse(responseCode = "200")
    ResponseEntity<Page<PetResponseDTO>> listAllPetsByFilter(
            @ModelAttribute PetFilter filter,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size);


    @Operation(summary = "Update Pet By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "status": 404,
                      "message": "Not Found",
                      "errors": [
                        { "field": "Not Found", "message": "Pet ID not found" }
                      ]
                    }
                """)))
    })
    ResponseEntity<PetResponseDTO> updatePetById(@RequestBody PetUpdateDTO petUpdateDTO,
                                                 @PathVariable(name = "id") UUID petId);



}
