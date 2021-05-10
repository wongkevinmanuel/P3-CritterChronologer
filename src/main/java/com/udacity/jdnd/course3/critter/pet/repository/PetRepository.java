package com.udacity.jdnd.course3.critter.pet.repository;


import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PetRepository extends JpaRepository<Pet,Long> {
    Pet mascotaxId(Long id);

    //List<Pet> mascotaXCliente(Customer customer);
    //List<Pet> findByIdIn(List<Long> petIds);
    //@Query("select id from pet where customer = :customer")
    //List<Long> mascotaIdxCliente(Customer customer);
}
