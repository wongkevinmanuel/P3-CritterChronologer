package com.udacity.jdnd.course3.critter.schedule.service;

import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ScheduleService {
    @Autowired
    private ScheduleRepository repository;

    //Create Calendarion para atender mascota
    //createSchedule
    public Long save(Schedule schedule){
        try{
            return repository.save(schedule).getId();
        }catch (IllegalArgumentException exception){
            throw new UnsupportedOperationException();
        }
    }
    //Obetner todos los calendarios creados
    //getAllSchedules()
    public List<Schedule> allSchedules(){
        try {
            return repository.findAll();
        }catch (IllegalArgumentException exception){
          throw new UnsupportedOperationException();
        }
    }

    //getScheduleForPet petId

    //getScheduleForEmployee employeeId
    //getScheduleForCustomer customerId

}
