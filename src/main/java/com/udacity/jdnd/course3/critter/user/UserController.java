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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import javax.validation.Valid;

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
    public ResponseEntity<CustomerDTO> customerInformation(@PathVariable long customerId){
        Optional<Customer> customer = clienteService.getCustomer(customerId);
        if (!customer.isPresent())
            return ResponseEntity.ok(new CustomerDTO());

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = customeraDTO(customer.get());
        return ResponseEntity.ok(new CustomerDTO());
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
        if(id<0)
            return ResponseEntity.ok(new String("Usuario Creado."));
        else
            return (ResponseEntity) ResponseEntity.badRequest();
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

    private Employee DTOaEmployee(EmployeeDTO employeeDTO, String nombrePropiedadAIgnorar){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee, nombrePropiedadAIgnorar);
        return employee;
    }

    private EmployeeDTO EmployeeaDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDayAvailable());
        return employeeDTO;
    }

    private EmployeeDTO employeeaDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }
    /*@PostMapping("/employee")
    public EmployeeDTO saveEmployee(@Valid
                                        @RequestBody EmployeeDTO employeeDTO) {
        boolean errorDatos;
        errorDatos = Objects.isNull(employeeDTO) ? true: false;
        if(errorDatos)
            throw new UnsupportedOperationException();

        if(employeeDTO.getName().isEmpty())
            throw new UnsupportedOperationException();

        Employee empleado = DTOaEmployee(employeeDTO,"id");
        Long id = empleadoService.guardar(empleado);
        if(id<= 0)
            return employeeDTO;

        employeeDTO.setId(id);
        log.info("Employee id:{} saved!", empleado.getId());
        return employeeDTO;
    }*/

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        if(isErrorPathVariable(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = empleadoService.empleado(employeeId);
        if (Objects.isNull(employee))
            throw new UnsupportedOperationException();

        log.info("Get employee ID:{} NAME:",employee.getId(),employee.getName());
        return employeeaDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public EmployeeDTO setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        if (Objects.isNull(daysAvailable) || Objects.isNull(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDayAvailable(daysAvailable);
        Employee updateEmployee = empleadoService.guardarDiasDisponibles(employee);
        log.info("Set Availability Employee ID:{}",updateEmployee.getId());
        return employeeaDTO(updateEmployee);
    }

    //Devolver todos los Empleados que tengan
    // las habilidades ingresadas y que estén disponibles en la fecha ingresada.
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        if(Objects.isNull(employeeRequestDTO))
            throw new UnsupportedOperationException();

        if (employeeRequestDTO.getSkills().isEmpty() || Objects.isNull(employeeRequestDTO.getDate()))
            return Collections.EMPTY_LIST;

        List<Employee> employees = empleadoService
                                        .findAllByDaysAvailableContainingEmployee(employeeRequestDTO.getDate()
                                                                                    ,employeeRequestDTO.getSkills());
        log.info("Find Employees For Service list size: {}", employees.size());

        return employees.stream().map(e -> EmployeeaDTO(e)).collect(Collectors.toList());
    }

}
