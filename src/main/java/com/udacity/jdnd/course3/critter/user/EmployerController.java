package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;
import com.udacity.jdnd.course3.critter.util.AyudaValidador;
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

    @Autowired
    private final EmployeeResourceAssember employeeResourceAssember;

    public EmployerController(EmployeeService employeeService, EmployeeResourceAssember employeeResourceAssember) {
       this.employeeService = employeeService;
        this.employeeResourceAssember = employeeResourceAssember;
    }

    private Employee DTOaEmployee(EmployeeDTO employeeDTO, String nombrePropiedadAIgnorar){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee, nombrePropiedadAIgnorar);
        return employee;
    }

    @PostMapping
    public ResponseEntity<EntityModel<EmployeeDTO>> saveEmployee(
                @Valid @RequestBody EmployeeDTO employeeDTO){

        try {
            if(employeeDTO.getName().isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (NullPointerException exception){
            throw new NullPointerException();
        }

        Employee employee = DTOaEmployee(employeeDTO, "id");
        employee = employeeService.guardar(employee);

        if (Objects.isNull(employee))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(employeeResourceAssember.toModel(employee));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EntityModel<EmployeeDTO>> getEmployee(
            @PathVariable(required = true) long employeeId){

        if (AyudaValidador.errorVarNulloLong(employeeId))
            throw new NullPointerException();

        Employee employee = employeeService.empleado(employeeId);

        if (Objects.isNull(employee))
            throw new NullPointerException();

        log.info("Get employee ID:{} NAME:{}"+ employee.getId(), employee.getName());

        if(!Objects.isNull(employee))
            return ResponseEntity.ok(employeeResourceAssember.toModel(employee));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EntityModel<EmployeeDTO>> setAvailability(@RequestBody
                 Set<DayOfWeek> dayOfWeekSet
                ,@PathVariable(required = true) long employeeId){

        if(Objects.isNull(dayOfWeekSet ) || Objects.isNull(employeeId))
            throw new NullPointerException();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDayAvailable(dayOfWeekSet);
        Employee updateEmployee = employeeService.guardarDiasDisponibles(employee);
        log.info("Set availabity Employee id:{}", updateEmployee);

        return ResponseEntity.ok(employeeResourceAssember.toModel(employee));
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

        return ResponseEntity.ok( new CollectionModel<>(
          employees.stream().map(employeeResourceAssember::toModel).collect(Collectors.toList())
            )
        );
    }

    @GetMapping("/all")
    public ResponseEntity< CollectionModel<EntityModel<EmployeeDTO>> > getAllEmployees(){
        List<Employee> employees = employeeService.buscarTodosEmpleados();
        if (employees.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        log.info("All employees, size list:{}", employees.size());

        return ResponseEntity.ok(
                new CollectionModel<>(
                        employees.stream().map(employeeResourceAssember::toModel)
                                                .collect(Collectors.toList())
                )
        );
    }
}
