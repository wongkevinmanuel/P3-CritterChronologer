package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.service.CustomerService;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import javax.validation.Valid;

/**
 * Incluye solicitudes tanto para clientes como para empleados. También estaría bien dividir esto en controladores
 * de usuario y cliente separados, aunque eso no es parte del alcance requerido para esta clase.
 */
@RestController
@ApiResponses( value={@ApiResponse(code=500, message="Internal Server Error server error response, The server encountered an unexpected condition that prevented it from fulfilling the request.")})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final CustomerService clienteService;

    @Autowired
    private final EmployeeService empleadoService;

    //k
    @Autowired
    private final PetService mascotaservicio;

    //k
    public UserController(CustomerService clienteService, EmployeeService empleadoService, PetService mascotaservicio) {
        this.clienteService = clienteService;
        this.empleadoService = empleadoService;
        this.mascotaservicio = mascotaservicio;
    }

    private Customer DTOaCustomer(CustomerDTO clienteDTO){
        Customer cliente = new Customer();
        cliente.setName(clienteDTO.getName());
        cliente.setNotes(clienteDTO.getNotes());
        cliente.setPhoneNumber(clienteDTO.getPhoneNumber());
        cliente.setAge(clienteDTO.getAge());
        return cliente;
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

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        boolean errorDatos;

        errorDatos = customerDTO == null ? true : false;
        if (errorDatos)
            throw new UnsupportedOperationException();

        if(customerDTO.getName().isEmpty() || customerDTO.getPhoneNumber().isEmpty())
            throw new UnsupportedOperationException();

        Customer customer = DTOaCustomer(customerDTO);
        Long id = clienteService.guardar(customer);

        if(id < 0)
            return customerDTO;

        //Entity save send response
        customerDTO.setId(id);
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
        //buscar los ids de las mascotas por cliente
        customer.setMascotas(mascotaservicio.mascotasXCliente(customer.getId()));

        if(Objects.isNull(customer))
            throw new NullPointerException();

        CustomerDTO customerDTO = customeraDTO(customer);
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = clienteService.clientes();
        if (customers.isEmpty())
            return Collections.EMPTY_LIST;

        return customers.stream().map(c -> customeraDTO(c)).collect(Collectors.toList());
    }

    //DTO a  employee
    private Employee DTOaEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        return employee;
    }
    //Employee a DTO
    private EmployeeDTO EmployeeaDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDayAvailable());
        return employeeDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        boolean errorDatos;
        errorDatos = Objects.isNull(employeeDTO) ? true: false;
        if(errorDatos)
            throw new UnsupportedOperationException();

        if(employeeDTO.getName().isEmpty())
            throw new UnsupportedOperationException();

        Employee empleado = DTOaEmployee(employeeDTO);
        Long id = empleadoService.guardar(empleado);
        if(id<= 0)
            return employeeDTO;

        employeeDTO.setId(id);
        return employeeDTO;
    }

    //@PostMapping("/employee/{employeeId}")
    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        if(isErrorPathVariable(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = empleadoService.empleado(employeeId);
        if (Objects.isNull(employee))
            throw new UnsupportedOperationException();

        return EmployeeaDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public EmployeeDTO setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        if (Objects.isNull(daysAvailable) || Objects.isNull(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDayAvailable(daysAvailable);
        Employee updateEmployee = empleadoService.guardarDiasDisponibles(employee);
        return EmployeeaDTO(updateEmployee);
    }

    //kevin, significa devolver todos los Empleados que tengan
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

        return employees.stream().map(e -> EmployeeaDTO(e)).collect(Collectors.toList());
    }

}
