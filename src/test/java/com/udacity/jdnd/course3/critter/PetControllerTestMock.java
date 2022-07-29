package com.udacity.jdnd.course3.critter;


import com.udacity.jdnd.course3.critter.pet.PetController;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import org.junit.Assert;
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
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    public void setup(){
        Pet pet = getPet();
        pet.setId(1L);
        given(petService.guardar(any())).willReturn(1L);
        given(petService.mascotaxId(any())).willReturn(pet);
        given(petService.mascotas()).willReturn(Collections.singletonList(pet));
    }


    @Test
    public void createPet() throws Exception{
        Pet pet = getPet();
        mvc.perform(
                post(new URI("/pet"))
                        .content(jsonPet.write(pet).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        //Deberia devolver una respuesta http de 201 cuando se a guardado el pet
        //https://stackoverflow.com/questions/1860645/create-request-with-post-which-response-codes-200-or-201-and-content
        //status().isCreated()
    }


    @Test
    public void getAllPet() {
        String URL = new StringBuilder("http://localhost:"+ port + "/pet/all").toString();

        Pet pet = this.getPet();

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        for(Pet item: pets){

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> requestEntity = new HttpEntity<Object>(item, headers);
            //Lanza la solicitud http
            ResponseEntity<Pet> entity = rest.exchange(URL
                    , HttpMethod.POST, requestEntity
                    ,Pet.class);

            pets.set(pets.indexOf(item), entity.getBody());
            Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
        }
    }

}
