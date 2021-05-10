package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository mascotaRepository;


    public Pet mascotaxId(Long id){
        return mascotaRepository.mascotaxId(id);
    }

    public Long guardar(Pet pet){
        return mascotaRepository.save(pet).getId();
    }

    public List<Pet> mascotas(){
        return mascotaRepository.findAll();
    }


}
