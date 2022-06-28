package com.udacity.jdnd.course3.critter.pet;

/**
 * Una lista de ejemplo de metadatos de tipo de mascota que podrían incluirse
 * en una solicitud para crear una mascota.
 */
public enum PetType {
    CAT(0),
    DOG(1),
    LIZARD(2),
    BIRD(3),
    FISH(4),
    SNAKE(5),
    OTHER(6);

    private Integer codigo;
    PetType(int i) {
        this.codigo=i;
    }
}
