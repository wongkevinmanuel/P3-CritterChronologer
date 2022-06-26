package com.udacity.jdnd.course3.critter.user.domain;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Table(name="Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private Long id;

    @Nationalized
    private String name;

    //Habilidades de los empleados
    @ElementCollection(targetClass = EmployeeSkill.class
                        , fetch = FetchType.LAZY)
    @Enumerated(EnumType.ORDINAL)
    private Set<EmployeeSkill> skills;

    //Dias disponibles
    @ElementCollection(targetClass = DayOfWeek.class
                        , fetch=FetchType.LAZY)
    @Enumerated(EnumType.ORDINAL)
    private Set<DayOfWeek> dayAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDayAvailable() {
        return dayAvailable;
    }

    public void setDayAvailable(Set<DayOfWeek> dayAvailable) {
        this.dayAvailable = dayAvailable;
    }
}
