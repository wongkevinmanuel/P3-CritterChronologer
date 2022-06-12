package com.udacity.jdnd.course3.critter.pet.domain;

import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.schedule.domain.Schedule;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import org.hibernate.annotations.Nationalized;
import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "pet")
public class Pet {
    //Propiedades
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private long id;

    @Column(name="type_pet_code")
    private @Enumerated(EnumType.STRING) PetType type;

    @Nationalized
    private String name;

    private LocalDate birthDate;

    @Nationalized
    private String notes;

    //private long ownerId;
    @ManyToOne(fetch = FetchType.LAZY)  //Muchas mascotas tiene un solo dueño
    @JoinColumn(name = "clientePropietario_id", referencedColumnName = "id")
    private Customer clientePropietario;//owner

    //Muchos mascotas tiene un calendario para ser atendidos
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
