package com.udacity.jdnd.course3.critter;


import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.udacity.jdnd.course3.critter.pet.PetController;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import org.junit.Assert;
import org.junit.Ignore;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    //la inyección del puerto
    @LocalServerPort
    private Integer port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Pet> jsonPet;

    @MockBean
    private PetService petService;

    private PetDTO getPetDTO(){
        PetDTO petDTO = new PetDTO();
        petDTO.setName("Ozzy");
        petDTO.setBirthDate(LocalDate.of(2022,03,06));
        petDTO.setNotes("Color cafe.");
        petDTO.setType(PetType.DOG);
        return petDTO;
    }

    private Pet getPet(){
        Pet pet = new Pet();
        pet.setName("Ozzy");
        pet.setBirthDate(LocalDate.of(2022,03,06));
        pet.setNotes("Color cafe.");
        pet.setType(PetType.DOG);
        return pet;
    }

    @Ignore
    @BeforeEach
    public void setup(){
        Pet pet = getPet();
        pet.setId(1L);
        given(petService.guardar(any())).willReturn(1L);
        given(petService.mascotaxId(any())).willReturn(pet);
        given(petService.mascotas()).willReturn(Collections.singletonList(pet));
    }
    @Test
    public void jwt() throws Exception{
        HashSet<String > data = new HashSet<String >();
        data.add("data1");

        when(petService.guardar(any())).thenReturn(1L);
        mvc.perform(new URI("/pet"))
    }
    @Test
    public void createPet() throws Exception{
        Pet pet = getPet();
        mvc.perform(
                post(new URI("/pet"))
                        .content(jsonPet.write(pet).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pet").exists());
        //Deberia devolver una respuesta http de 201 cuando se a guardado el pet
        //https://stackoverflow.com/questions/1860645/create-request-with-post-which-response-codes-200-or-201-and-content
        //status().isCreated()
    }

    @Test
    public void getAllPetMvcMock() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:"+ port + "/pet/all")
                                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void getPetsRecordReportMvcMock() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:"+ port+"/pet/records/reportJasper/5")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void getPetsRecordReport(){
        String URL = new StringBuilder("http://localhost:"+ port+"/pet/records/reportJasper/5").toString();

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String,String> valueMapHeader = new LinkedMultiValueMap<>();
        valueMapHeader.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<Object>(null,valueMapHeader);

        ResponseEntity<?> responseEntity = restTemplate.exchange(
                URL,HttpMethod.GET,requestEntity,byte[].class);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void listPets(){
        String URL = new StringBuilder("http://localhost:" + port + "/pet").toString();
        PetDTO petDTO = this.getPetDTO();
        RestTemplate rest = new RestTemplate();
        MultiValueMap<String,String> valuesHeaders = new LinkedMultiValueMap<String,String>();
        valuesHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpHeaders headers = new HttpHeaders(valuesHeaders);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(petDTO, headers);
        ResponseEntity<PetDTO> responseEntity = rest.exchange(URL,HttpMethod.POST, requestEntity, PetDTO.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //Get all pet list
        String URL2 = new StringBuilder("http://localhost:" + port + "/pet/listPets").toString();
        RestTemplate rest2 = new RestTemplate();
        HttpEntity<?> requestEntity2 = new HttpEntity<Object>(null, headers);
        ResponseEntity<?> responseEntity2 = rest.exchange(
                URL2
                ,HttpMethod.GET
                , requestEntity2
                , List.class);

        Assert.assertNotNull(responseEntity2);
        Assert.assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
    }

    @Test
    public void getAllPet() {
        String URL = new StringBuilder("http://localhost:"+ port + "/pet").toString();

        PetDTO petDTO = this.getPetDTO();

        List<PetDTO> pets = new ArrayList<>();
        pets.add(petDTO);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> requestEntity = new HttpEntity<Object>(petDTO, headers);
        //Lanza la solicitud http para save pet
        ResponseEntity<PetDTO> entity = rest.exchange(URL
                   , HttpMethod.POST, requestEntity
                   , PetDTO.class);

        pets.set(pets.indexOf(petDTO), entity.getBody());
        Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

        //Get all pet
        ResponseEntity<?> entity2 = rest.exchange(URL + "/all"
                , HttpMethod.GET
                , null
                , List.class);

        Assert.assertEquals(HttpStatus.OK, entity2.getStatusCode());
        List<PetDTO> pets2 = (List) entity2.getBody();
        Assert.assertEquals(pets.size(), pets2.size());
    }

    private CustomerDTO getCustomerDTO(){
        CustomerDTO  customerDTO = new CustomerDTO();
        customerDTO.setAge(27);
        customerDTO.setName("Kevin");
        customerDTO.setNotes("Cliente serio");
        customerDTO.setPhoneNumber("0941629388");
        return customerDTO;
    }

    //Chek this, not return customer ***************************************
    @Test
    public void getPetsByOwner(){
        //Save Customer Owner
        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO =getCustomerDTO();
        String URL = new StringBuilder("http://localhost:"+port+"user/customer").toString();
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<Object>(customerDTO, headers);
        ResponseEntity<CustomerDTO> response = rest.exchange(URL
                ,HttpMethod.POST
                ,request
                ,CustomerDTO.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        //Save Pet with id customer owner
        URL = new StringBuilder("http://localhost:"+port+"/pet").toString();

        PetDTO petDTO = getPetDTO();
        petDTO.setOwnerId(response.getBody().getId());

        HttpEntity<?> request2 = new HttpEntity<Object>(petDTO, headers);
        ResponseEntity<PetDTO> responsePet = rest.exchange(URL
                ,HttpMethod.POST
                ,request2
                ,PetDTO.class);

        Assert.assertNotNull(responsePet);
        Assert.assertEquals(HttpStatus.OK, responsePet.getStatusCode());

        //Equals Id pet vs Id pet get the request Controller
        URL = new StringBuilder("http://localhost:" + port + "/user/customer/pet/"+responsePet.getBody().getId()).toString();
        RestTemplate rest2 = new RestTemplate();
        ResponseEntity<CustomerDTO> responseCustomerDTO = rest2.exchange(URL
                ,HttpMethod.GET
                ,null
                ,CustomerDTO.class);

        Assert.assertNotNull(responseCustomerDTO);
        Assert.assertEquals(HttpStatus.OK, responseCustomerDTO.getStatusCode());
        Assert.assertEquals(responsePet.getBody().getOwnerId() , responseCustomerDTO.getBody().getId());
    }
}
