package com.udacity.jdnd.course3.critter.schedule;


import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.schedule.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
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
        scheduleDTO.setId(schedule.getId());
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
        if (!schedule.getActivities().isEmpty()){
            scheduleDTO.setActivities(schedule.getActivities());
        }
        return scheduleDTO;
    }

    private Schedule schedule;
    private <T> T createObjectForType(T type,Long id){
        if(type instanceof Pet){
            Pet pet = (Pet) type;
            pet.setId(id);
        }
        if(type instanceof Employee){
            Employee employee = (Employee) type;
            employee.setId(id);
        }
        return type;
    }
    private void scheduleDTOASchedule(ScheduleDTO scheduleDTO){
        schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        //Long Ids Convert to PetIds
        schedule.setPets(new ArrayList<>());
        scheduleDTO.getPetIds().forEach(
                                pId -> schedule.getPets().add(createObjectForType( new Pet(),pId)));

        //Convert to employee
        schedule.setEmployees(new ArrayList<>());
        scheduleDTO.getEmployeeIds().forEach(
                                eId -> schedule.getEmployees().add(createObjectForType(new Employee(),eId) ));
    }
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        if(Objects.isNull(scheduleDTO))
            throw new UnsupportedOperationException();
        if(Objects.isNull(scheduleDTO.getDate()))
            throw new UnsupportedOperationException();

        scheduleDTOASchedule(scheduleDTO);
        Long id = scheduleService.save(schedule);

        if(id<=0)
            return scheduleDTO;

        //Entity save send response with the ids save in Data base
        scheduleDTO.setId(id);
        return scheduleDTO;
    }


    @GetMapping("/allschedules")
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules =scheduleService.allSchedules();
        if (Objects.isNull(schedules))
            return new ArrayList<ScheduleDTO>(Collections.EMPTY_LIST);

        return schedules.stream().map(s -> scheduleAScheduleDTO(s)).collect(Collectors.toList());
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
