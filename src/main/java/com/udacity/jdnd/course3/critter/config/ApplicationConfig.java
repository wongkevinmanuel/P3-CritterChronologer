package com.udacity.jdnd.course3.critter.config;

import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfig {

    private final UserRepository repository;

    public ApplicationConfig(UserRepository repository) {
        this.repository = repository;
    }

    @Bean
    public UserDetailsService userDetailsService(){
         return username -> repository.findByUserName(username)
                 .orElseThrow(()-> new UsernameNotFoundException("User nor found"));
    }
}
