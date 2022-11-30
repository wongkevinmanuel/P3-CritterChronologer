package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.reposity.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository empleadoRepository;

    public List<Employee> buscarTodosEmpleados(){
        return empleadoRepository.findAll();
    }
    public Long guardar(Employee empleado){
        try{
            return empleadoRepository.save(empleado).getId();
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException();
        }
    }

    public Employee empleado(Long id){
        try {
            return empleadoRepository.getOne(id);
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException();
        }
    }

    public Employee guardarDiasDisponibles(Employee employee){
        try{
            Employee employeeToUpdate = empleadoRepository.getOne(employee.getId());
            employeeToUpdate.setDayAvailable(employee.getDayAvailable());
            return empleadoRepository.save(employeeToUpdate);
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException(exception);
        }
    }

    List<Employee> availableEmployees = new ArrayList<>();
    private void employeeSkillsContainsRequiredSkills(List<Employee> employees,Set<EmployeeSkill> skills){
        for (Employee e: employees) {
            if(e.getSkills().containsAll(skills)){
                availableEmployees.add(e);
            }
        }
    }
    public List<Employee> findAllByDaysAvailableContainingEmployee(LocalDate date, Set<EmployeeSkill> skills){//DayOfWeek day, Set<EmployeeSkill> skills){
        List<Employee> employees;
        try{
           employees = empleadoRepository.findAll();
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException(exception);
        }
        employeeSkillsContainsRequiredSkills(employees,skills);
        return availableEmployees;
    }


}
