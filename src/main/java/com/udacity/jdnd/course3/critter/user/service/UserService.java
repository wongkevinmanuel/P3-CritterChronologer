package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.login.requests.domain.User;
import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    public Long guardar(User usuario){
        try{
            return userRepository.save(usuario).getId();
        }catch (IllegalArgumentException exp){
            throw exp;
        }
    }

    public List<User> usuarios(){
        return userRepository.findAll();
    }


    public User findUser(String name){
        try {
             return userRepository.findByUserName(name);
        }catch (IllegalArgumentException exception)
        {
            throw new EmptyResultDataAccessException(1);
        }
    }

}
