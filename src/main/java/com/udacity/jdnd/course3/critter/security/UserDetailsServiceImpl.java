package com.udacity.jdnd.course3.critter.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

//Define un metodo para recuperar el usuario de la bd
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user =repository.findByUsername(userName);

        if (Objects.isNull(user))
            throw new UsernameNotFoundException(userName);

        org.springframework.security.core.userdetails.User userSpring = new User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );

        return userSpring;
    }
}
