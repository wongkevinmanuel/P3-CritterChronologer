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

    @Query("select id,birthDate,name,notes,type from Pet ")
    List<Pet> mascotas();
    @Query("from Pet where clientePropietario.id = :idPropietario")
    List<Pet> buscarMascotaXCliente(@Param("idPropietario") Long idPropietario);

}
