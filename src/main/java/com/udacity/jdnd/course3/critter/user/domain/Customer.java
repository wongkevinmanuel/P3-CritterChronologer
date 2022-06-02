package com.udacity.jdnd.course3.critter.user.domain;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private long id;

    @Nationalized
    private String name;

    @Nationalized
    private String  phoneNumber;

    @Nationalized
    private String notes;

    //Un dueño tiene muchas mascotas
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Pet> pets;
}
