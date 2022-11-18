package com.udacity.jdnd.course3.critter.login.repository;

import com.udacity.jdnd.course3.critter.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByUserName(String username);

}
