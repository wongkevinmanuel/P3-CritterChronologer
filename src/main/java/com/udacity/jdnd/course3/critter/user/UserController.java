package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.login.domain.User;
import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;

import com.udacity.jdnd.course3.critter.login.domain.User;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.service.CustomerService;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;

import com.udacity.jdnd.course3.critter.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


/**
 * Incluye solicitudes tanto para clientes
 */
@RestController
@RequestMapping("/user")
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
    private UserService usuarioServicio;

    public UserController(CustomerService clienteService, EmployeeService empleadoService, PetService mascotaservicio) {
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.mascotaservicio = mascotaservicio;
    }

    private Customer DTOaCustomer(CustomerDTO customerDTO, String nombrePropiedadAIgnorar){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer, nombrePropiedadAIgnorar);
        return customer;
    }

    private CustomerDTO customeraDTO(Customer customer,String nombrePropiedadAIgnorar){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO,nombrePropiedadAIgnorar);
        if (Objects.isNull(customer.getMascotas()))
            return customerDTO;

        if(!customer.getMascotas().isEmpty()) {
            customerDTO.setPetIds(new ArrayList<>());
            for (Pet p:customer.getMascotas() ) {
                customerDTO.getPetIds().add(p.getId());
            }
        }
        return customerDTO;
    }
    private CustomerDTO customeraDTO(Customer customer){
        CustomerDTO clienteDTO = new CustomerDTO();
        clienteDTO.setName(customer.getName());
        clienteDTO.setNotes(customer.getNotes());
        clienteDTO.setPhoneNumber(customer.getPhoneNumber());
        clienteDTO.setId(customer.getId());
        clienteDTO.setAge(customer.getAge());

        if (customer.getMascotas() != null){
            if(!customer.getMascotas().isEmpty()) {
                clienteDTO.setPetIds(new ArrayList<>());
                for (Pet p:customer.getMascotas() ) {
                    clienteDTO.getPetIds().add(p.getId());
                }
            }
        }
        return clienteDTO;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<EntityModel <CustomerDTO> > customerInformation(@PathVariable long customerId){
        Optional<Customer> customer = clienteService.getCustomer(customerId);
        if (!customer.isPresent()) {
            log.error("Error in get information Customer:"+customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = customeraDTO(customer.get());
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping("/userSave")
    public ResponseEntity saveUser(@RequestBody CustomerDTO customerDTO){
        if (customerDTO == null? true:false)
            throw new UnsupportedOperationException();

        if(customerDTO.getName().isEmpty())
            throw new UnsupportedOperationException();

        User usuario = new User();
        usuario.setFirstName(customerDTO.getName());
        Long id = usuarioServicio.guardar(usuario);
        if(id>0)
            return ResponseEntity.ok(usuario);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){//@Valid CustomerDTO customerDTO){
        boolean errorDatos;

        errorDatos = customerDTO == null ? true : false;
        if (errorDatos)
            throw new UnsupportedOperationException();

        if(customerDTO.getName().isEmpty() || customerDTO.getPhoneNumber().isEmpty())
            throw new UnsupportedOperationException();

        Customer customer = DTOaCustomer(customerDTO,"petIds");
        Long id = clienteService.guardar(customer);

        if(id < 0)
            return customerDTO;

        //Entity save send response
        customerDTO.setId(id);
        log.info("Customer id:{} save",customerDTO.getId());
        return customerDTO;
    }

    private boolean isErrorPathVariable(long Id){
        try{
            if(Objects.isNull(Id))
                return true;

            Long.valueOf(Id);
            return false;
        }catch (Exception exception){
            return true;
        }
    }
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        if(isErrorPathVariable(petId))
            throw new UnsupportedOperationException();

        Customer customer = clienteService.buscarClienteXMascota(petId);
        customer.setMascotas(mascotaservicio.mascotasXCliente(customer.getId()));

        if(Objects.isNull(customer))
            throw new NullPointerException();

        log.info("Owner id is {} By Pet id:{}",customer.getId(),petId);
        CustomerDTO customerDTO = customeraDTO(customer,"petIds");
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = clienteService.clientes();
        if (customers.isEmpty())
            return Collections.EMPTY_LIST;

        log.info("All customers, size list: {}",customers.size());
        return customers.stream().map(c -> customeraDTO(c,"petIds")).collect(Collectors.toList());
    }

}
