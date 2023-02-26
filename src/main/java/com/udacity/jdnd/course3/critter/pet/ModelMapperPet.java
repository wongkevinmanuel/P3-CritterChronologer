package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperPet {

    private ModelMapper modelMapper;

    public ModelMapperPet() {
    }

    public PetDTO covertEntityToDto(Pet pet){
        modelMapper.getConfiguration().setMatchingStrategy(
                MatchingStrategies.LOOSE);

        PetDTO petDTO = new PetDTO();
        petDTO = modelMapper.map(pet,PetDTO.class);

        return petDTO;
    }
}
