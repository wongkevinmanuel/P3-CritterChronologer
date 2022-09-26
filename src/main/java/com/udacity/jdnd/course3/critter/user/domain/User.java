package com.udacity.jdnd.course3.critter.user.domain;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name="User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Nationalized
    @Column(name="userName", length = 8)
    private String userName;
    @Nationalized
    private String password;
    @Nationalized
    private String firstName;
    @Nationalized
    private String lastName;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
