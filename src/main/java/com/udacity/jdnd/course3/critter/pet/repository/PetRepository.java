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
    Pet mascotaXNombre(String name);
    //Pet mascotaxId(Long id);
    @Query("select id,birthDate,name,notes,type from Pet ")
    List<Pet> mascotas();
    @Query("select id,birthDate,name,notes,type from Pet p where p.clientePropietario = :idPropietario")
    List<Pet> mascotasXCliente(@Param("idPropietario") Long idPropietario);
    @Query("from Pet p where p.id in (:ids)")
    List<Pet> mascotasXIds(@Param("ids") List<Long> idsMascotas);

    //List<Pet> findByIdIn(List<Long> petIds);
}
