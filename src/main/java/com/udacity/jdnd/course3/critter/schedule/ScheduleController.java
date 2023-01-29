package com.udacity.jdnd.course3.critter.schedule;


import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.schedule.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import com.udacity.jdnd.course3.critter.util.AyudaValidador;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja solicitudes web relacionadas con Horarios.
 */
@RestController
@RequestMapping("/schedule")
@ApiResponses(value={
        @ApiResponse(code= 400, message = "Bad request, please follow the API documentation for the proper request format.")
        ,@ApiResponse(code = 401, message = "Due to security contraints, your access request cannot be authorized.")
        ,@ApiResponse(code = 404, message = "Not found, check if the resource is saved.")
        ,@ApiResponse(code=500, message = "The server is down.")
})
public class ScheduleController {

    public static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    @Autowired
    private final ScheduleService scheduleService;

    @Autowired
    private final ScheduleResourceAssember scheduleResourceAssember;
    public ScheduleController(ScheduleService scheduleService, ScheduleResourceAssember scheduleResourceAssember) {
        this.scheduleService = scheduleService;
        this.scheduleResourceAssember = scheduleResourceAssember;
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
            schedule.getPets().forEach(p -> { listIdPet.add(p.getId());});//addLongIdList(p.getId()));
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
    private Schedule scheduleDTOASchedule(ScheduleDTO scheduleDTO){

        Schedule schedule = new Schedule();
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
        return schedule;
    }
    @PostMapping
    public ResponseEntity<EntityModel< ScheduleDTO> >
            createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        if(Objects.isNull(scheduleDTO))
            throw new UnsupportedOperationException();
        if(Objects.isNull(scheduleDTO.getDate()))
            throw new UnsupportedOperationException();

        Schedule schedule = scheduleDTOASchedule(scheduleDTO);
        schedule = scheduleService.save2(schedule);

        if(Objects.isNull( schedule ))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        //Entity save send response with the ids save in Data base
        log.info("Save Schedule ID:{}", schedule.getId());
        return ResponseEntity.ok(scheduleResourceAssember.toModel(schedule));
    }

    @GetMapping("/allSchedules")
    public ResponseEntity< CollectionModel< EntityModel<ScheduleDTO> > > getAllSchedules() {

        List<Schedule> schedules = scheduleService.allSchedules();
        if (Objects.isNull(schedules) )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        log.info("All Schedules size list:{}", schedules.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                        schedules.stream().map(scheduleResourceAssember::toModel)
                                .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<CollectionModel<EntityModel<ScheduleDTO> > >
    getScheduleForPet(@PathVariable(required = true) long petId) {

        if(AyudaValidador.errorVarNulloLong(petId))
            throw new UnsupportedOperationException();

        List<Schedule> schedules = scheduleService.scheduleXPet(petId);
        if(schedules.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//Collections.EMPTY_LIST;

        log.info("Get all Schedule for pet, size list schedule {}.", schedules.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                    schedules.stream().map(scheduleResourceAssember::toModel)//s -> scheduleAScheduleDTO(s))
                            .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<CollectionModel<EntityModel< ScheduleDTO> > >
        getScheduleForEmployee(@PathVariable(required = true) long employeeId) {

        if(AyudaValidador.errorVarNulloLong(employeeId))
            throw new UnsupportedOperationException();

        List<Schedule> schedules = scheduleService.schedulesXEmployee(employeeId);
        if(schedules.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        log.info("Get all Schedule For employee, size list schedule {}.",schedules.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                        schedules.stream().map( scheduleResourceAssember::toModel)
                                            .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CollectionModel<EntityModel<ScheduleDTO> > >
        getScheduleForCustomer(@PathVariable(required = true) long customerId) {

        if(AyudaValidador.errorVarNulloLong(customerId))
            throw new UnsupportedOperationException();

        List<Schedule> schedules = scheduleService.scheduleXCostumer(customerId);
        if(schedules.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        log.info("Get all Schedule for customer, size list schedule {}.", schedules.size());
        return ResponseEntity.ok(new CollectionModel<>(
                schedules.stream().map(
                        scheduleResourceAssember::toModel).collect(Collectors.toList())));
    }
}
