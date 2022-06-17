package com.udacity.jdnd.course3.critter.user.domain;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Nationalized
    private String name;

    @Nationalized
    private String  phoneNumber;

    @Nationalized
    private String notes;

    private int age;

    @Column(name="cedula",length = 10)
    private String cedula;

    //Un dueño tiene muchas mascotas
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "clientePropietario")
    private List<Pet> mascotas;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    //public List<Pet> getPets() {
    //    return pets;
    //}

    //public void setPets(List<Pet> pets) {
    //    this.pets = pets;
    //}
}
