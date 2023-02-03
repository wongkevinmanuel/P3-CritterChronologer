package com.udacity.jdnd.course3.critter.login.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.javac.util.List;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name="User")
public class User implements UserDetails {

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
    @Column (nullable = false)
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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //METODOS DE LA IMPLEMENTACION
    //return una lista de roles
    //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //La app solo puede tener un rol
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
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
