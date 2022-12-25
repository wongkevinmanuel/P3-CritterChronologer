package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ScheduleResourceAssember  implements RepresentationModelAssembler<ScheduleDTO, EntityModel<ScheduleDTO> > {

    @Override
    public EntityModel<ScheduleDTO> toModel(ScheduleDTO entity) {
        EntityModel<ScheduleDTO> resourceScheduleDTO = new EntityModel<>(entity);

        Link linkToCreateSchedule = WebMvcLinkBuilder
                .linkTo(methodOn(ScheduleController.class)
                        .createSchedule(new ScheduleDTO() )).withSelfRel();

        Link linkToAllSchedules = WebMvcLinkBuilder
                .linkTo(methodOn(ScheduleController.class)
                        .getAllSchedules()).withRel("schedule/allschedules");

        resourceScheduleDTO.add(linkToCreateSchedule);
        resourceScheduleDTO.add(linkToAllSchedules);
        return resourceScheduleDTO;
    }

    @Override
    public CollectionModel<EntityModel<ScheduleDTO>> toCollectionModel(Iterable<? extends ScheduleDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
