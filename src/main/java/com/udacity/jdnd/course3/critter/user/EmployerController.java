package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.service.EmployeeService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

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
}
