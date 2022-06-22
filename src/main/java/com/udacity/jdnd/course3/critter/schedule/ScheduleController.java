package com.udacity.jdnd.course3.critter.schedule;


import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 * Maneja solicitudes web relacionadas con Horarios.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
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
    private List<Long> listIdPet= new ArrayList<>();
    private void addLongIdList(Long id){
        listIdPet.add(id);
    }
    private ScheduleDTO scheduleAScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDate(schedule.getDate());
        if (!schedule.getPets().isEmpty()) {
            schedule.getPets().forEach(p -> addLongIdList(p.getId()));
            scheduleDTO.setPetIds(listIdPet);
        }
        if (!schedule.getEmployees().isEmpty()) {
            List<Long> listIdEmployees = new ArrayList<>();
            schedule.getEmployees().stream().forEach( e -> listIdEmployees.add(e.getId()));
            scheduleDTO.setEmployeeIds(listIdEmployees);
        }
        if (!schedule.getActivities().isEmpty())
        {

        }
        return scheduleDTO;
    }
    private Schedule scheduleDTOASchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        return schedule;
    }
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        return null;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        if(isErrorPathVariable(petId))
            throw new UnsupportedOperationException();

        List<Schedule> schedules = scheduleService.scheduleXPet(petId);
        if(schedules.isEmpty())
            return Collections.EMPTY_LIST;

        return schedules.stream().map(s -> scheduleAScheduleDTO(s)).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }
}
