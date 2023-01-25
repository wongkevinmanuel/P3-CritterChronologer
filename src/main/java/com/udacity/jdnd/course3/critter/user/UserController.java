package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.service.CustomerService;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;

import com.udacity.jdnd.course3.critter.user.service.UserService;
import com.udacity.jdnd.course3.critter.util.AyudaValidador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


/**
 * Incluye solicitudes tanto para clientes
 */
@RestController
@RequestMapping("/user")
//@RequestMapping("${env.BASE_USER_PATH}")
@ApiResponses(value={
        @ApiResponse(code= 400, message = "Bad request, please follow the API documentation for the proper request format.")
        ,@ApiResponse(code = 401, message = "Due to security contraints, your access request cannot be authorized.")
        ,@ApiResponse(code = 404, message = "Not found, check if the resource is saved.")
        ,@ApiResponse(code=500, message = "The server is down.")
})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final CustomerService clienteService;
    @Autowired
    private final EmployeeService empleadoService;
    @Autowired
    private final PetService mascotaservicio;

    @Autowired
    private final CustomerResourceAssembler assembler;
    @Autowired
    private UserService usuarioServicio;

    public UserController(CustomerService clienteService
            , EmployeeService empleadoService
            , PetService mascotaservicio
            , CustomerResourceAssembler assembler) {
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.mascotaservicio = mascotaservicio;
        this.assembler = assembler;
    }

    private Customer DTOaCustomer(CustomerDTO customerDTO, String nombrePropiedadAIgnorar){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer, nombrePropiedadAIgnorar);
        return customer;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<EntityModel <CustomerDTO> > customerInformation(@PathVariable long customerId){
        if(AyudaValidador.errorVarNulloLong(customerId))
            throw new UnsupportedOperationException();

        Optional<Customer> customer = clienteService.getCustomer(customerId);

        if (!customer.isPresent()) {
            log.error("Error in get information Customer:"+customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok( assembler.toModel(customer.get()));
    }


    @PostMapping("/customer")
    public ResponseEntity<EntityModel<CustomerDTO>> saveCustomer(@RequestBody CustomerDTO customerDTO){

        if (Objects.isNull(customerDTO))
            throw new UnsupportedOperationException();

        if(customerDTO.getName().isEmpty() || customerDTO.getPhoneNumber().isEmpty())
            throw new UnsupportedOperationException();

        Customer customer = DTOaCustomer(customerDTO,"petIds");
        customer = clienteService.guardar(customer);

        if(Objects.isNull(customer))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else
            if(customer.getId() <= 0)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        //Entity save send response
        log.info("Customer id:{} save",customer.getId());
        return ResponseEntity.ok(assembler.toModel(customer));
    }

    @GetMapping("/customer/pet/{petId}")
    public ResponseEntity<EntityModel<CustomerDTO>> getOwnerByPet(@PathVariable long petId){
        if(AyudaValidador.errorVarNulloLong(petId))
            throw new UnsupportedOperationException();

        Customer customer = clienteService.buscarClienteXMascota(petId);
        customer.setMascotas(mascotaservicio.mascotasXCliente(customer.getId()));

        if(Objects.isNull(customer))
            throw new NullPointerException();

        log.info("Owner id is {} By Pet id:{}",customer.getId(),petId);
        return ResponseEntity.ok( assembler.toModel(customer));
    }

    @GetMapping("/customers")
    public ResponseEntity< CollectionModel<EntityModel<CustomerDTO> > > getAllCustomers(){
        List<Customer> customers = clienteService.clientes();
        if (customers.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        log.info("All customers, size list: {}",customers.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                        customers.stream().map(assembler::toModel).collect(Collectors.toList())
                )
        );
    }
}
