package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 * Maneja las solicitudes web relacionadas con los Usuarios.
 *
 * Incluye solicitudes tanto para clientes como para empleados. También estaría bien dividir esto en controladores
 * de usuario y cliente separados, aunque eso no es parte del alcance requerido para esta clase.
 */
@RestController
@ApiResponses( value={@ApiResponse(code=500, message="Internal Server Error server error response, The server encountered an unexpected condition that prevented it from fulfilling the request.")})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final CustomerService clienteService;

    public UserController(CustomerService clienteService) {
        this.clienteService = clienteService;
    }

    private Customer DTOaCustomer(CustomerDTO clienteDTO){
        Customer cliente = new Customer();
        cliente.setName(clienteDTO.getName());
        cliente.setNotes(clienteDTO.getNotes());
        cliente.setPhoneNumber(clienteDTO.getPhoneNumber());
        return cliente;
    }

    private CustomerDTO customeraDTO(Customer customer){
        CustomerDTO clienteDTO = new CustomerDTO();
        clienteDTO.setName(customer.getName());
        clienteDTO.setNotes(customer.getNotes());
        clienteDTO.setPhoneNumber(customer.getPhoneNumber());
        //clienteDTO.setPetIds();
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
    private boolean isErrorPathVariable(long petId){
        try{
            if(Objects.isNull(petId))
                return true;

            Long.valueOf(petId);
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

        if(Objects.isNull(customer))
            throw new UnsupportedOperationException();

        CustomerDTO customerDTO = customeraDTO(customer);
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

}
