package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 * Maneja solicitudes web relacionadas con mascotas.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private final PetService mascotaService;

    PetController(PetService petS){
        this.mascotaService = petS;
    }

    private Pet DTOaPet(PetDTO petDTO){
        Pet mascota = new Pet();
        mascota.setName(petDTO.getName());
        mascota.setNotes(petDTO.getNotes());
        mascota.setType(petDTO.getType());
        mascota.setBirthDate(petDTO.getBirthDate());
        //mascota.setOwnerId(petDTO.getOwnerId());
        return mascota;
    }

    private PetDTO petaDTO(Pet pet){
        PetDTO mascotaDTO = new PetDTO();
        mascotaDTO.setId(pet.getId());
        mascotaDTO.setName(pet.getName());
        mascotaDTO.setNotes(pet.getNotes());
        mascotaDTO.setType(pet.getType());
        mascotaDTO.setBirthDate(pet.getBirthDate());
        //mascotaDTO.setOwnerId(pet.getOwnerId());
        return mascotaDTO;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = DTOaPet(petDTO);
        Long id = mascotaService.guardar(pet);
        if (id==0)
            throw new UnsupportedOperationException();

        //Good
        petDTO.setId(id);
        return petDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = null;
        pet = mascotaService.mascotaxId(petId);
        if (pet == null )
            return null;

        return petaDTO(pet);
    }

    // para despues implementar
    @GetMapping("/all")
    public List<PetDTO> getPets(){
        List<Pet> pets = new ArrayList<>();

        pets = mascotaService.mascotas();

        if(pets.isEmpty())
            return null;// BUSCAR MEJOR OPCION Q NULL

        return pets.stream().map(p -> petaDTO(p)).collect(Collectors.toList());
    }

    //Obtener mascota por propietario
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        throw new UnsupportedOperationException();
    }
}
