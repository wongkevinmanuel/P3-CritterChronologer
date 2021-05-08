package com.udacity.jdnd.course3.critter.pet;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 * Maneja solicitudes web relacionadas con mascotas.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    //Obtener mascota por propietario
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        throw new UnsupportedOperationException();
    }
}
