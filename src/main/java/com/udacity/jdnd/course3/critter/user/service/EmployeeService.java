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

    public Employee guardarDisponibilidad(Long id, Set<DayOfWeek> diasSemana){
        try{
            Employee employee = new Employee();
            employee.setId(id);
            employee.setDayAvailable(diasSemana);
            return empleadoRepository.save(employee);
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException(exception);
        }
    }

}
