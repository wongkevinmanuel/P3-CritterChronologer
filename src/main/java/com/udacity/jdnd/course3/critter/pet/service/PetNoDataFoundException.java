package com.udacity.jdnd.course3.critter.pet.service;

public class PetNoDataFoundException extends RuntimeException {

    public PetNoDataFoundException(){
    }

    public PetNoDataFoundException(String error){
        super("No data found. "+error);
    }
}
