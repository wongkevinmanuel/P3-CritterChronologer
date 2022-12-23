package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user/employee")
@ApiResponses(value = {
        @ApiResponse(code= 400, message = "Bad request, please follow the API documentation for the proper request format.")
})
public class EmployerController {
    private static final Logger log = LoggerFactory.getLogger(EmployerController.class);

    @Autowired
    private final EmployeeService employeeService;

    public EmployerController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private Employee DTOaEmployee(EmployeeDTO employeeDTO, String nombrePropiedadAIgnorar){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee, nombrePropiedadAIgnorar);
        return employee;
    }
    @PostMapping
    public ResponseEntity<EntityModel<EmployeeDTO>> saveEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO){
        if(Objects.isNull(employeeDTO))
            throw new NullPointerException();

        if(employeeDTO.getName().isEmpty())
            throw new NullPointerException();

        Employee employee = DTOaEmployee(employeeDTO, "id");
        employeeService.guardar(employee);

        if (employee.getId() > 0) {
            employeeDTO.setId(employee.getId());
            return ResponseEntity.ok(employeeDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    private EmployeeDTO employeeaDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<EntityModel<EmployeeDTO>> getEmployee(
            @PathVariable long employeeId){
        if (isErrorPathVariable(employeeId))
            throw new NullPointerException();

        Employee employee = employeeService.empleado(employeeId);
        if (Objects.isNull(employee))
            throw new NullPointerException();

        log.info("Get employee ID:{} NAME:{}"+ employee.getId(), employee.getName());

        if(!Objects.isNull(employee))
            return ResponseEntity.ok(employeeaDTO(employee));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EntityModel<EmployeeDTO>> setAvailability(@RequestBody
                 Set<DayOfWeek> dayOfWeekSet
                ,@PathVariable long employeeId)
    {
        if(Objects.isNull(dayOfWeekSet ) || Objects.isNull(employeeId))
            throw new NullPointerException();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDayAvailable(dayOfWeekSet);
        Employee updateEmployee = employeeService.guardarDiasDisponibles(employee);
        log.info("Set availabity Employee id:{}", updateEmployee);
        return ResponseEntity.ok(employeeaDTO(employee));
    }

    private EmployeeDTO EmployeeaDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDayAvailable());
        return employeeDTO;
    }

    //Devolver todos los Empleados que tengan
    // las habilidades ingresadas y que estén disponibles en la fecha ingresada.
    @GetMapping("/availability")
    public ResponseEntity<CollectionModel<EntityModel<EmployeeDTO> > >
    findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO){
        if(Objects.isNull(employeeRequestDTO))
            throw new NullPointerException();

        if(employeeRequestDTO.getSkills().isEmpty()
                || Objects.isNull(employeeRequestDTO.getDate()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<Employee> employees = employeeService
                .findAllByDaysAvailableContainingEmployee(employeeRequestDTO.getDate()
                        ,employeeRequestDTO.getSkills());

        log.info("Find Employee for service list size:{}", employees.size());
        List<EmployeeDTO> employeesDTO =  employees.stream().map(e -> EmployeeaDTO(e)).collect(Collectors.toList());
        return ResponseEntity.ok( new CollectionModel<>(
          employeesDTO.stream().map(asse)
        ));
        //employeesDTO
    }

    @GetMapping("/all")
    public ResponseEntity< CollectionModel<EmployeeDTO> > getAllEmployees(){
        List<Employee> employees = employeeService.buscarTodosEmpleados();
        if (employees.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        log.info("All employees, size list:{}", employees.size());
        List<EmployeeDTO> employeesDTO = employees.stream().map(e -> employeeaDTO(e)).collect(Collectors.toList());
        return ResponseEntity.ok(employeesDTO);
    }
}
