package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.dto.CustomerDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CustomerResourceAssembler
        implements RepresentationModelAssembler<Customer, EntityModel<CustomerDTO> > {

    @Override
    public CollectionModel toCollectionModel(Iterable entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<CustomerDTO> toModel(Customer customer) {
        EntityModel<CustomerDTO> resourceCustomer = new EntityModel<>(new CustomerDTO());//CustomerDTO>(customerDTO);

        //set
        resourceCustomer.getContent().setId(customer.getId());
        resourceCustomer.getContent().setName(customer.getName());
        resourceCustomer.getContent().setNotes(customer.getNotes());
        resourceCustomer.getContent().setAge(customer.getAge());
        resourceCustomer.getContent().setPhoneNumber(customer.getPhoneNumber());
        // IDs pets
        //resourceCustomer.getContent().setPetIds();
        //customer.getMascotas();

        Link linkACustomerId = WebMvcLinkBuilder
                .linkTo( methodOn(UserController.class).customerInformation(customer.getId())).withSelfRel();

        Link linkATodosCustomers = WebMvcLinkBuilder
                .linkTo(methodOn(UserController.class).getAllCustomers()).withRel("user/customers");
        resourceCustomer.add(linkACustomerId);
        resourceCustomer.add(linkATodosCustomers);

        return resourceCustomer;
    }
}
