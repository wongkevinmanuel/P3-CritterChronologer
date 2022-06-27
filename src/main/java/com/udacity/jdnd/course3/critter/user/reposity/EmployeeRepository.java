package com.udacity.jdnd.course3.critter.user.reposity;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    //@Query("from Employee e where e.dayAvailable = :day")
    //List<Employee> findAllByDaysAvailableContaining(@Param("day") DayOfWeek day);

}
