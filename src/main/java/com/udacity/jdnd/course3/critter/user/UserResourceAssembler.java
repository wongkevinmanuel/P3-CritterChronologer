package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.login.domain.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


public class UserResourceAssembler implements RepresentationModelAssembler<User, EntityModel<User> > {

    @Override
    public CollectionModel toCollectionModel(Iterable entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<User> toModel(User entity) {
        return null;
    }
}
