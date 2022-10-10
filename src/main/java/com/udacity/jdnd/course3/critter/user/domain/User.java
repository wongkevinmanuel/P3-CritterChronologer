package com.udacity.jdnd.course3.critter.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name="User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @JsonProperty
    private Long id;

    @Nationalized
    @Column(name="userName", length = 8, nullable = true, unique = true)
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

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
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
}
