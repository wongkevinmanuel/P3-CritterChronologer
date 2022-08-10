package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@Component
public class PetResourceAssembler implements RepresentationModelAssembler <Pet, EntityModel<Pet> > {

    @Override
    public EntityModel<Pet> toModel(Pet pet) {
        EntityModel<Pet> resourcePet = new EntityModel<Pet>(pet);
        Link linkToPetId = WebMvcLinkBuilder.linkTo( methodOn(PetController.class).getPet(pet.getId() )).withSelfRel();
        Link linkToPets = WebMvcLinkBuilder.linkTo( methodOn(PetController.class).getPets()).withRel("pet/all");
        resourcePet.add(linkToPetId);
        resourcePet.add(linkToPets);
        return  resourcePet;
        // Se crea un link que apunta recurso que daria como resultado la
        //invocacion del metodo correspondiente en el controlador, uso
        //de metodos estaticos
        //return EntityModel.of();
        // linkTo( methodOn(PetController.class).get(pet.getId()) ).withSelfRel() ,
        // linkTo(methodOn(PetController.class).list()).withRel("pets");
        // );
    }
}
