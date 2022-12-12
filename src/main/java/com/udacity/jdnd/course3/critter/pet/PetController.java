package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.pet.utils.CustomJasperReport;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//USO DE LIBRERIA DE HATEOAS
import org.springframework.hateoas.*;


import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/pet")
@ApiResponses(value={
        @ApiResponse(code= 400, message = "Bad request, please follow the API documentation for the proper request format.")
        ,@ApiResponse(code = 401, message = "Due to security contraints, your access request cannot be authorized.")
        ,@ApiResponse(code = 403, message = "Unauthorized request. The client does not have access rights to the content.")
        ,@ApiResponse(code = 404, message = "Not found, check if the resource is saved.")
        ,@ApiResponse(code = 405, message = "The request HTTP method is known but has been disabled and cannot be used for that resource")
        ,@ApiResponse(code=500, message = "The server is down.")
        ,@ApiResponse(code= 501 , message = "The HTTP method is not supported by the server and cannot be handled.")
})
public class PetController  extends JasperReportController{

    public static final Logger log = LoggerFactory
                                        .getLogger(PetController.class);
    @Autowired
    private final PetService mascotaService;

    public PetController(PetService petS){
        this.mascotaService = petS;
    }

    private Pet DTOaPet(PetDTO petDTO,String nombrePropiedadAIgnorar){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet,nombrePropiedadAIgnorar );
        if (petDTO.getOwnerId() != 0){
            Customer customer = new Customer();
            customer.setId(petDTO.getId());
            pet.setClientePropietario(customer);
        }
        return pet;
    }
    private PetDTO petaDTO(Pet pet){
        PetDTO mascotaDTO = new PetDTO();
        mascotaDTO.setId(pet.getId());
        mascotaDTO.setName(pet.getName());
        mascotaDTO.setNotes(pet.getNotes());
        mascotaDTO.setType(pet.getType());
        mascotaDTO.setBirthDate(pet.getBirthDate());

        if(!Objects.isNull(pet.getClientePropietario())){
            if (pet.getClientePropietario().getId() != 0L)
                mascotaDTO.setOwnerId(pet.getClientePropietario().getId());
            else
                mascotaDTO.setOwnerId(0);
        }
        //mascotaDTO.setOwnerId(pet.getClientePropietario().getId());
        return mascotaDTO;
    }

    private PetDTO petaDTO(Pet pet, String nombrePropiedadAIgnorar){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO, nombrePropiedadAIgnorar);
        petDTO.setOwnerId(
                !Objects.isNull(pet.getClientePropietario())
                        ? pet.getClientePropietario().getId(): 0);
        return petDTO;
    }

    //Usando ResponseEntity y Hateoas
    @PostMapping
    public PetDTO savePet(@Valid @RequestBody PetDTO petDTO)  {
        boolean errorDatos;
        errorDatos = Objects.isNull(petDTO);
        if(errorDatos)
            throw new UnsupportedOperationException();

        if (petDTO.getName().isEmpty()  || petDTO.getNotes().isEmpty())
            throw new UnsupportedOperationException();

        Pet pet = DTOaPet(petDTO,"ownerId");
        Long id = mascotaService.guardar(pet);

        if (id<=0)
            return petDTO;

        //Entity save send response
        log.info("Created pet id: {}"+id);
        petDTO.setId(id);
        return petDTO;
    }

    @Autowired
    private PetResourceAssembler assembler;

    @GetMapping("/all2")
    ResponseEntity < CollectionModel<EntityModel<Pet>> > list(){
        //List<EntityModel<Pet> > resourcesPet = null;
        List<Pet> pets = mascotaService.mascotas();
        if (pets.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        log.info("All pets, size list: {}" + pets.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                        pets.stream().map(assembler::toModel).collect(Collectors.toList())
                ));
    }

    @GetMapping("/petEntity/{petId}")
    ResponseEntity< EntityModel<Pet> > getPetEntity(@PathVariable long petId) {
        Pet pet = mascotaService.mascotaxId(petId);
        if (Objects.isNull(pet))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        log.info("Pet Id: {} Name: {}" ,pet.getId(),pet.getName() );
        return ResponseEntity.ok(assembler.toModel(pet));//assembler.toModel(pet);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity <CollectionModel< EntityModel<PetDTO> > > getPetsByOwner(@PathVariable long ownerId) {

        List<Pet> pets = mascotaService.mascotasXCliente(ownerId);
        if(pets.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        List<PetDTO> petsDTO = pets.stream().map(p -> petaDTO(p)).collect(Collectors.toList());
        log.info("Pets size: {}" ,pets.size());
        return ResponseEntity.ok(
                new CollectionModel<>(
                   petsDTO.stream().map(assembler::toModel).collect(Collectors.toList())
                )
        );
    }

    //All for created Report using jasper
    private HttpHeaders generateHeader(String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", fileName);
        return headers;
    }
    @GetMapping("/records/{petId}")
    ResponseEntity<byte[]> getPetRecordReport(@PathVariable(required = false) long petId){
        CustomJasperReport report = mascotaService.generatePetReport(petId);
        setJasperReport(report);
        //Configuracion a formato pdf
        HttpHeaders headers = generateHeader(report.getOutPutFilename());

        try {
            return new ResponseEntity<>(responseReportPDF(), headers,HttpStatus.OK);
            //return respondReportOutPutWithoutHeader();
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/records/reportJasper/{numberPet}")
    public ResponseEntity<byte[]> getPetsRecordReport(@PathVariable(required = false) int numberPet){

        CustomJasperReport report = mascotaService.generatePetsReport(numberPet);
        setJasperReport(report);

        //Establecer configuracion del formato a PDF
        HttpHeaders headers = generateHeader(report.getOutPutFilename());

        try{
            return new ResponseEntity<byte[]>(responseReportPDF(),headers,HttpStatus.OK );
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
