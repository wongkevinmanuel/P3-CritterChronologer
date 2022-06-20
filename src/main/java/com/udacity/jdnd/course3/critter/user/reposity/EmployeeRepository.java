package com.udacity.jdnd.course3.critter.user.reposity;

import com.udacity.jdnd.course3.critter.user.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
