package com.udacity.jdnd.course3.critter.user.reposity;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c join fetch c.mascotas")
    Customer findOwnerByPet(@Param("idMascota") Long idMascota);
}
