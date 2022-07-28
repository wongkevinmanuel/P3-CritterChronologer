package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository mascotaRepository;

    public Long guardar(Pet pet){
        try{
            return mascotaRepository.save(pet).getId();
        }catch (IllegalArgumentException exception) {
            throw new PetNotFoundException(exception);
        }
    }
    public List<Pet> mascotas(){
        try{
            List<Pet> pets = mascotaRepository.findAll();
            if (pets.isEmpty())
                throw new PetNotFoundException();

            return pets;
        }catch (IllegalArgumentException exception){
            throw new PetNotFoundException(exception);
        }
    }
    public List<Pet> mascotasXCliente(Long id){
        try {
            return mascotaRepository.buscarMascotaXCliente(id);
        }catch (IllegalArgumentException exception){
            throw new PetNotFoundException(exception);
        }
    }

    public Pet mascotaxId(Long id){
        try {
            Pet pet = mascotaRepository.getOne(id);
            if (Objects.isNull(pet))
                //throw new PetNoDataFoundException();
                throw new NullPointerException();

            if(pet.getId() == 0)
                throw new PetNoDataFoundException();

            return pet;
        }catch (IllegalArgumentException exception){
            throw new PetNotFoundException(exception);
        }
    }
}
