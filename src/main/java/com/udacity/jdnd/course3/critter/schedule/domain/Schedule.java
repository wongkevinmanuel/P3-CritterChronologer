package com.udacity.jdnd.course3.critter.schedule.domain;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.user.domain.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable = false)
    private Long id;

    @Nationalized
    private LocalDate date;

    @ManyToMany(fetch=FetchType.LAZY
                ,cascade = CascadeType.MERGE)
    @JoinTable(name="schedule_employee"
                ,joinColumns = @JoinColumn(name="id")
                ,inverseJoinColumns = @JoinColumn(name="employee_id"))
    private List<Employee> employees = new ArrayList<>();

    @ManyToMany(fetch=FetchType.LAZY
                , cascade = CascadeType.MERGE)
    @JoinTable(name="schedule_pet", joinColumns = @JoinColumn(name="id")
            ,inverseJoinColumns = @JoinColumn(name="pet_id"))
    private List<Pet> pets = new ArrayList<>();


    @ElementCollection(targetClass = EmployeeSkill.class)
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> activities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Set<EmployeeSkill> getActivities() {
        return activities;
    }

    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities;
    }

}
