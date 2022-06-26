package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.user.reposity.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository empleadoRepository;

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

}
