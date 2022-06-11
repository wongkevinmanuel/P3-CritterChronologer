package com.udacity.jdnd.course3.critter.schedule.domain;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.user.domain.Employee;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                ,cascade = CascadeType.ALL)
    @JoinTable(name="schedule_employee"
                ,joinColumns = @JoinColumn(name="id")
                ,inverseJoinColumns = @JoinColumn(name="employee_id"))
    private List<Employee> employees = new ArrayList<>();

    @ManyToMany(fetch=FetchType.LAZY
                , cascade = CascadeType.ALL)
    @JoinTable(name="schedule_pet", joinColumns = @JoinColumn(name="id")
            ,inverseJoinColumns = @JoinColumn(name="pet_id"))
    private List<Pet> pets = new ArrayList<>();

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



}
