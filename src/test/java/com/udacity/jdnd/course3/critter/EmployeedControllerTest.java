package com.udacity.jdnd.course3.critter;

import com.google.common.collect.Sets;
import com.google.j2objc.annotations.AutoreleasePool;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.EmployerController;
import com.udacity.jdnd.course3.critter.user.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

//Conjunto de pruebas funcionales
@Transactional
@SpringBootTest(classes = CritterApplication.class)
public class EmployeedControllerTest {

    @Autowired
    private EmployerController employerController;
    private static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("TestEmployee");
        employeeDTO.setSkills(
                Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING)
        );
        return employeeDTO;
    }
    @Test
    public void testCreateEmployee(){
        EmployeeDTO employeeDTO = createEmployeeDTO();
        ResponseEntity<EmployeeDTO> newEmployee = employerController.saveEmployee(employeeDTO);
        ResponseEntity<EmployeeDTO> retrievedEmployee = employerController.getEmployee(newEmployee.getBody().getId());
        Assertions.assertEquals(employeeDTO.getSkills(), newEmployee.getBody().getSkills());
        Assertions.assertEquals(newEmployee.getBody().getId(), retrievedEmployee.getBody().getId());
        Assertions.assertTrue(retrievedEmployee.getBody().getId() > 0);
    }

}
