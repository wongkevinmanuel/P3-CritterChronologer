package com.udacity.jdnd.course3.critter.login.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.management.relation.Role;
import javax.persistence.*;
import org.hibernate.annotations.Nationalized;


@Entity
@Table(name="User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @JsonProperty
    private Long id;

    @Nationalized
    @Column( length = 8, unique = true)
    @JsonProperty
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @Nationalized
    private String password;

    @Nationalized
    @JsonProperty
    private String firstName;
    @Nationalized
    @JsonProperty
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setUserName(String nameuser) {
        this.userName = nameuser;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }
}