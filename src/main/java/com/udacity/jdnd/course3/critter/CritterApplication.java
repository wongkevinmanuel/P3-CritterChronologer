package com.udacity.jdnd.course3.critter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Launches the Spring application. Unmodified from starter code.
 * Primer Commit y push
 */
@SpringBootApplication
public class CritterApplication {

	//Repositorios utiles
	//https://github.com/poonamgp14/critter/blob/master/starter/critter/src/test/java/com/udacity/jdnd/course3/critter/CritterFunctionalTest.java
	//https://gitlab.com/henq8/javawebdevelopment/-/blob/master/project03/starter/critter/src/main/java/com/udacity/jdnd/course3/critter/pet/service/PetDTOHelper.java
	public static void main(String[] args) {
		SpringApplication.run(CritterApplication.class, args);
	}

}
