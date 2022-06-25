package com.udacity.jdnd.course3.critter.schedule.service;

import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository repository;

    //Create Calendarion para atender mascota
    public Long save(Schedule schedule){
        try{
            schedule.setId(null);
            return repository.save(schedule).getId();
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException();
        }
    }
    //Obetner todos los calendarios creados
    public List<Schedule> allSchedules(){
        try {
            return repository.findAll();
        }catch (IllegalArgumentException exception){
          throw new UnsupportedOperationException();
        }
    }
    //Obtener calendario por el id de una mascota
    public List<Schedule> scheduleXPet(Long petId){
        try {
            return repository.schedulesXPet(petId);
        }catch (IllegalArgumentException exception){
            throw  new UnsupportedOperationException();
        }
    }
    //Obtener el calendario de un empleador por su id
    public List<Schedule> schedulesXEmployee(Long employeeId){
        try {
            return repository.schedulesXEmployee(employeeId);
        }catch (IllegalArgumentException exception){
            throw  new UnsupportedOperationException();
        }
    }
    //Obetener el calendarion de una mascota por el cliente
    public List<Schedule> scheduleXCostumer(Long customerId){
        try {
            return repository.getScheduleXCustomer(customerId);
        }catch (IllegalArgumentException exception){
            throw  new UnsupportedOperationException();
        }
    }
}
