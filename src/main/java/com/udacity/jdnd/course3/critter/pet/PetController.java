package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private final PetService mascotaService;

    public PetController(PetService petS){
        this.mascotaService = petS;
    }

    private Pet DTOaPet(PetDTO petDTO){
        Pet mascota = new Pet();
        mascota.setName(petDTO.getName());
        mascota.setNotes(petDTO.getNotes());
        mascota.setType(petDTO.getType());
        mascota.setBirthDate(petDTO.getBirthDate());
        if(petDTO.getOwnerId() != 0) {
            Customer customer = new Customer();
            customer.setId(petDTO.getOwnerId());
            mascota.setClientePropietario(customer);
        }
            return mascota;
    }

    private Pet DTOaPet(PetDTO petDTO,String nombrePropiedadAIgnorar){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet,nombrePropiedadAIgnorar );
        if (petDTO.getOwnerId() != 0){
            Customer customer = new Customer();
            customer.setId(petDTO.getId());
            pet.setClientePropietario(customer);
        }
        return pet;
    }
    private PetDTO petaDTO(Pet pet){
        PetDTO mascotaDTO = new PetDTO();
        mascotaDTO.setId(pet.getId());
        mascotaDTO.setName(pet.getName());
        mascotaDTO.setNotes(pet.getNotes());
        mascotaDTO.setType(pet.getType());
        mascotaDTO.setBirthDate(pet.getBirthDate());
        mascotaDTO.setOwnerId(pet.getClientePropietario().getId());
        return mascotaDTO;
    }

    private PetDTO petaDTO(Pet pet, String nombrePropiedadAIgnorar){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO, nombrePropiedadAIgnorar);


        petDTO.setOwnerId(
                !Objects.isNull(pet.getClientePropietario())
                        ? pet.getClientePropietario().getId(): 0);
        return petDTO;
    }

    @PostMapping
    public PetDTO savePet(@Valid @RequestBody PetDTO petDTO)  {
        boolean errorDatos;
        errorDatos = Objects.isNull(petDTO) ? true: false;
        if(errorDatos)
            throw new UnsupportedOperationException();

        if (petDTO.getName().isEmpty()  || petDTO.getNotes().isEmpty())
            throw new UnsupportedOperationException();

        Pet pet = DTOaPet(petDTO,"ownerId");
        Long id = mascotaService.guardar(pet);

        if (id<=0)
            return petDTO;

        //Entity save send response
        petDTO.setId(id);
        return petDTO;
    }

    @GetMapping("/all")
    public List<PetDTO> getPets(){
        List<Pet> pets = mascotaService.mascotas();
        if(pets.isEmpty())
            return new ArrayList<PetDTO>(Collections.EMPTY_LIST);

        return pets.stream().map(p -> petaDTO(p,"ownerId")).collect(Collectors.toList());
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = null;
        pet = mascotaService.mascotaxId(petId);
        if (pet == null )
            return null;

        return petaDTO(pet);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = mascotaService.mascotasXCliente(ownerId);
        if(pets.isEmpty())
            return Collections.EMPTY_LIST;

        return pets.stream().map(p -> petaDTO(p)).collect(Collectors.toList());
    }
}
