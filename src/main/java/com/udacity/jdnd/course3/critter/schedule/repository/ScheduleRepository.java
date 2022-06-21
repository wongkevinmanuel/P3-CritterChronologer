package com.udacity.jdnd.course3.critter.schedule.repository;

import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository <Schedule,Long> {
    //@Query("select  from Schedule ")
    List<Schedule> schedules();
    //Query("buscar ***")
    //List<>
}
