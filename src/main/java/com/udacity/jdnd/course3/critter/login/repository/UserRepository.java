package com.udacity.jdnd.course3.critter.login.repository;

import com.udacity.jdnd.course3.critter.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByUserName(String username);
    Optional<User> findByFirstName(String nameFirst);
}
