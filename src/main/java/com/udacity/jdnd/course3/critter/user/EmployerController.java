package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jfree.data.time.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Set;

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
    @PostMapping("/employee")
    public ResponseEntity<EmployeeDTO> saveEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO){
        if(Objects.isNull(employeeDTO))
            throw new UnsupportedOperationException();

        if(employeeDTO.getName().isEmpty())
            throw new UnsupportedOperationException();

        Employee employee = DTOaEmployee(employeeDTO, "id");
        employeeService.guardar(employee);

        if (employee.getId() <= 0)
            return ResponseEntity.ok(new EmployeeDTO());

        return ResponseEntity.badRequest().build();
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
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(
            @PathVariable long employeeId){
        if (isErrorPathVariable(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = employeeService.empleado(employeeId);
        if (Objects.isNull(employee))
            throw new UnsupportedOperationException();

        log.info("Get employee ID:{} NAME:{}"+ employee.getId(), employee.getName());

        return ResponseEntity.ok(
                employeeaDTO(employee));
    }

    @PutMapping("employee/{employeeId}")
    public ResponseEntity<EmployeeDTO> setAvailability(@RequestBody Set<DayOfWeek> dayOfWeekSet
                ,@PathVariable long employeeId)
    {
        if(Objects.isNull(dayOfWeekSet ) || Objects.isNull(employeeId))
            throw new UnsupportedOperationException();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setDayAvailable(dayOfWeekSet);
        Employee updateEmployee = employeeService.guardarDiasDisponibles(employee);
        log.info("Set availabity Employee id:{}", updateEmployee)
        ;
        return ResponseEntity.ok(employeeaDTO(employee));
    }
}
