package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ScheduleResourceAssember  implements RepresentationModelAssembler<ScheduleDTO, EntityModel<ScheduleDTO> > {

    @Override
    public EntityModel<ScheduleDTO> toModel(ScheduleDTO entity) {
        EntityModel<ScheduleDTO> resourceScheduleDTO = new EntityModel<>(entity);

        Link enlaceAScheduleDTOID = WebMvcLinkBuilder
                .linkTo(methodOn(ScheduleController.class).)
        return null;
    }

    @Override
    public CollectionModel<EntityModel<ScheduleDTO>> toCollectionModel(Iterable<? extends ScheduleDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
