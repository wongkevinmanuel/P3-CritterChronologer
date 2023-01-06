package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.schedule.ScheduleController;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.dto.EmployeeDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeResourceAssember
        implements RepresentationModelAssembler<Employee, EntityModel<EmployeeDTO> > {

    @Override
    public EntityModel<EmployeeDTO> toModel(Employee employee) {
        EntityModel<EmployeeDTO> resourceEmployeeDTO = new EntityModel<>(new EmployeeDTO());

        //Set
        BeanUtils.copyProperties(employee,resourceEmployeeDTO);


        Link linkToSaveEmployee = WebMvcLinkBuilder
                .linkTo(methodOn(EmployerController.class)
                        .saveEmployee( new EmployeeDTO())).withSelfRel();

        Link linkToAllEmployee = WebMvcLinkBuilder
                .linkTo(methodOn(EmployerController.class)
                        .getAllEmployees()).withRel("user/employee/all");

        resourceEmployeeDTO.add(linkToSaveEmployee);
        resourceEmployeeDTO.add(linkToAllEmployee);
        return resourceEmployeeDTO;
    }

}
