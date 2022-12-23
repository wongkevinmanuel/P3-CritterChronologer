package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.dto.EmployeeDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class EmployeeResourceAssember  implements RepresentationModelAssembler<EmployeeDTO, EntityModel<EmployeeDTO> > {

    @Override
    public EntityModel<EmployeeDTO> toModel(EmployeeDTO entity) {
        return null;
    }

    @Override
    public CollectionModel<EntityModel<EmployeeDTO>> toCollectionModel(Iterable<? extends EmployeeDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
