package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ScheduleResourceAssember
        implements RepresentationModelAssembler<Schedule, EntityModel<ScheduleDTO> > {


    private List<Long> listIdPet= new ArrayList<>();
    private ScheduleDTO scheduleAScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        if (!schedule.getPets().isEmpty()) {
            schedule.getPets().forEach(p -> { listIdPet.add(p.getId());});
            scheduleDTO.setPetIds(listIdPet);
        }
        if (!schedule.getEmployees().isEmpty()) {
            List<Long> listIdEmployees = new ArrayList<>();
            schedule.getEmployees().stream().forEach( e -> listIdEmployees.add(e.getId()));
            scheduleDTO.setEmployeeIds(listIdEmployees);
        }
        if (!schedule.getActivities().isEmpty()){
            scheduleDTO.setActivities(schedule.getActivities());
        }
        return scheduleDTO;
    }

    @Override
    public EntityModel<ScheduleDTO> toModel(Schedule entity) {
        EntityModel<ScheduleDTO> resourceScheduleDTO =
                                new EntityModel<>(scheduleAScheduleDTO(entity));

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
    public CollectionModel<EntityModel<ScheduleDTO>> toCollectionModel(Iterable<? extends Schedule> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
