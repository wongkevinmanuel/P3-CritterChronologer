package com.udacity.jdnd.course3.critter.pet.domain;

import com.udacity.jdnd.course3.critter.pet.PetType;
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
    private long id;

    //@Column(name="type_code", length = 1)
    @Column(name="type_code")
    private @Enumerated(EnumType.STRING) PetType type;

    @Nationalized
    private String name;

    private LocalDate birthDate;

    @Nationalized
    private String notes;

    //private long ownerId;
    @ManyToOne //Muchas mascotas tiene un solo dueño
    @JoinColumn(name = "ownerId")
    private Customer customer;

    //Metodos de acceso
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
