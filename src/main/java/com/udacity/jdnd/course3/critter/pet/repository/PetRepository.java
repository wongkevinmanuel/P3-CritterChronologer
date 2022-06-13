package com.udacity.jdnd.course3.critter.pet.repository;


import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PetRepository extends JpaRepository<Pet,Long> {
    //Pet mascotaxId(Long id);
    @Query("select id,birthDate,name,notes,type from Pet ")
    List<Pet> mascotas();
    @Query("select id,birthDate,name,notes,type from Pet p where p.clientePropietario = :idPropietario")
    List<Pet> mascotaXCliente(@Param("idPropietario") Long idPropietario);
    //List<Pet> findByIdIn(List<Long> petIds);
    //@Query("select id from pet where customer = :customer")
    //List<Long> mascotaIdxCliente(Customer customer);
}
