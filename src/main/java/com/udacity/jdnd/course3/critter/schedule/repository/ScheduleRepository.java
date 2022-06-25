package com.udacity.jdnd.course3.critter.schedule.repository;

import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository <Schedule,Long> {
    //getScheduleForPet petId getScheduleForEmployee employeeId getScheduleForCustomer customerId
    //@Query("from Schedule s left join fetch s.pets p where p.id=:petId")
    @Query("from Schedule s inner join s.pets p where p.id = :petId")
    List<Schedule> schedulesXPet(@Param("petId") Long petId);

    //getScheduleForEmployee employeeId
    @Query("from Schedule  s inner join s.employees e where e.id = :employeeId")
    List<Schedule> schedulesXEmployee(@Param("employeeId") Long employeeId);

}
