package com.udacity.jdnd.course3.critter.security;

import com.udacity.jdnd.course3.critter.user.domain.User;
import com.udacity.jdnd.course3.critter.user.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

//Define un metodo para recuperar el usuario de la bd
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName);

        if (Objects.isNull(user))
            throw new UsernameNotFoundException(userName);

        org.springframework.security.core.userdetails.User userSpring =
                new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );

        return userSpring;
    }
}
