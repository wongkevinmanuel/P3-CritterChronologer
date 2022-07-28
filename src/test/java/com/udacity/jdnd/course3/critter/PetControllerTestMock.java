package com.udacity.jdnd.course3.critter;


import com.udacity.jdnd.course3.critter.pet.PetController;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
/*
* Se utiliza el enfoque donde no iniciar el servidor
* en absoluto, sino probar solo la capa debajo de eso
* , donde Spring maneja la solicitud HTTP entrante
* y se la pasa a su controlador. De esa forma, se
* usa casi toda la pila y se llamará a su código
* exactamente de la misma manera que si estuviera
* procesando una solicitud HTTP real.
* Para llevar a cabo esto se usa MockMvc de Spring
* y solicite que se inyecte usando la anotación
* @AutoConfigureMockMvc en el caso de prueba.
* */


// para iniciar el servidor con un puerto aleatorio con
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PetControllerTestMock {

    private static final Logger log= LoggerFactory.getLogger(PetController.class);

    //la inyección del puerto con
    @LocalServerPort
    private Integer port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Pet> jsonPet;

    @MockBean
    private PetService petService;

    private Pet getPet(){
        Pet pet = new Pet();
        pet.setName("Ozzy");
        pet.setBirthDate(LocalDate.of(2022,03,06));
        pet.setNotes("Color cafe.");
        pet.setType(PetType.DOG);
        return pet;
    }

    @BeforeEach
    @Test
    public void setup(){
        Pet pet = getPet();
        pet.setId(1L);
        given(petService.guardar(any())).willReturn(1L);
        given(petService.mascotaxId(any())).willReturn(pet);
        given(petService.mascotas()).willReturn(Collections.singletonList(pet));
    }
}
