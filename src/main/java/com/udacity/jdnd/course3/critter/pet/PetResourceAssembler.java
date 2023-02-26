package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PetResourceAssembler
        implements RepresentationModelAssembler<Pet, EntityModel<PetDTO> > {


    /*
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

        if(!Objects.isNull(pet.getClientePropietario())){
            if (pet.getClientePropietario().getId() != 0L)
                mascotaDTO.setOwnerId(pet.getClientePropietario().getId());
            else
                mascotaDTO.setOwnerId(0);
        }
        //mascotaDTO.setOwnerId(pet.getClientePropietario().getId());
        return mascotaDTO;
    }
    */

    @Autowired
    private ModelMapperPet modelMapperPet;

    public PetResourceAssembler(ModelMapperPet modelMapperPet) {
        this.modelMapperPet = modelMapperPet;
    }

    @Override
    public EntityModel<PetDTO> toModel(Pet pet) {
        EntityModel<PetDTO> resourcePetDTO = new EntityModel<>(new PetDTO());

        resourcePetDTO.getContent().setId(pet.getId());
        resourcePetDTO.getContent().setName(pet.getName());
        resourcePetDTO.getContent().setNotes(pet.getNotes());
        resourcePetDTO.getContent().setType(pet.getType());
        resourcePetDTO.getContent().setBirthDate(pet.getBirthDate());

        if(!Objects.isNull(pet.getClientePropietario())){
            if (pet.getClientePropietario().getId() != 0L)
                resourcePetDTO.getContent().setOwnerId(pet.getClientePropietario().getId());
            else
                resourcePetDTO.getContent().setOwnerId(0);
        }

        Link linkToPetId = WebMvcLinkBuilder.linkTo( methodOn(PetController.class)
                                                                .getPet(pet.getId() )).withSelfRel();
        Link linkToPets = WebMvcLinkBuilder.linkTo( methodOn(PetController.class)
                                                                .getPets()).withRel("pet/all");
        resourcePetDTO.add(linkToPetId);
        resourcePetDTO.add(linkToPets);
        return  resourcePetDTO;
        // Se crea un link que apunta recurso que daria como resultado la
        //invocacion del metodo correspondiente en el controlador, uso
        //de metodos estaticos
        //return EntityModel.of();
        // linkTo( methodOn(PetController.class).get(pet.getId()) ).withSelfRel() ,
        // linkTo(methodOn(PetController.class).list()).withRel("pets");
        // );
    }

    /*
    public EntityModel<PetDTO> toModel(PetDTO petDTO){
        EntityModel<PetDTO> resourcePetDTO = new EntityModel<PetDTO>(petDTO);
        Link linkToPetId = WebMvcLinkBuilder.linkTo( methodOn(PetController.class)
                .getPet(petDTO.getId() )).withSelfRel();
        Link linkToPets = WebMvcLinkBuilder.linkTo( methodOn(PetController.class)
                .getPets()).withRel("pet/all");
        resourcePetDTO.add(linkToPetId);
        resourcePetDTO.add(linkToPets);
        return  resourcePetDTO;
    }*/
}
