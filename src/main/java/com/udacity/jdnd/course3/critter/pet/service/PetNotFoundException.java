package com.udacity.jdnd.course3.critter.pet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Pet not found")
public class PetNotFoundException extends RuntimeException{
    private Throwable causa;
    public PetNotFoundException(){}

    public PetNotFoundException(Throwable causa){
        super(causa.getClass().getName() + "-->"
                + causa.getMessage());
        this.causa = causa;
    }

    public Throwable getCausa(){
        return this.causa;
    }
}
