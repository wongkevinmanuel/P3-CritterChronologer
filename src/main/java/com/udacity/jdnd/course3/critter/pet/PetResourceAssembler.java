package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PetResourceAssembler implements RepresentationModelAssembler <Pet, EntityModel<Pet> > {

    @Override
    public EntityModel<Pet> toModel(Pet entity) {
        return null;
    }
}
