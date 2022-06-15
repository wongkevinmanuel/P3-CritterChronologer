package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository mascotaRepository;

    public Long guardar(Pet pet){
        try{
            return mascotaRepository.save(pet).getId();
        }catch (IllegalArgumentException exception) {
            throw new PetNotFoundException(exception.getMessage() + "c.PetService.m.guardar");
        }
    }
    public List<Pet> mascotas(){
        try{
            return mascotaRepository.findAll();
        }catch (IllegalArgumentException exception){
            throw new PetNotFoundException(exception.getMessage() + "c.PetService.m.mascotas");
        }
    }
    public Pet mascotaxId(Long id){
        //mascotaRepository.getOne(id);
        return mascotaRepository.getOne(id); // null;//mascotaRepository.mascotaxId(id);
    }

}
