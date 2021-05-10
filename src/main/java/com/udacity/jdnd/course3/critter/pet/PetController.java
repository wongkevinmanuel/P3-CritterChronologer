package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 * Maneja solicitudes web relacionadas con mascotas.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService mascotaService;

    private Pet DTOaPet(PetDTO petDTO){
        Pet mascota = null;
        return mascota;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet mascota = DTOaPet(petDTO);
        Long idGuardo = mascotaService.guardar(mascota);
        if (idGuardo != 0)
        {
            petDTO.setId(idGuardo);
            return petDTO;
        }else{
            throw new UnsupportedOperationException();
        }
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
