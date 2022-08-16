package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.pet.domain.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.pet.utils.CustomJasperReport;
import com.udacity.jdnd.course3.critter.user.domain.Customer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
//USO DE LIBRERIA DE HATEOAS
import org.springframework.hateoas.*;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/pet")
public class PetController  extends JasperReportController{
    @Autowired
    private final PetService mascotaService;

    public PetController(PetService petS){
        this.mascotaService = petS;
    }

    private Pet DTOaPet(PetDTO petDTO){
        Pet mascota = new Pet();
        mascota.setName(petDTO.getName());
        mascota.setNotes(petDTO.getNotes());
        mascota.setType(petDTO.getType());
        mascota.setBirthDate(petDTO.getBirthDate());
        if(petDTO.getOwnerId() != 0) {
            Customer customer = new Customer();
            customer.setId(petDTO.getOwnerId());
            mascota.setClientePropietario(customer);
        }
            return mascota;
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

    @PostMapping
    public PetDTO savePet(@Valid @RequestBody PetDTO petDTO)  {
        boolean errorDatos;
        errorDatos = Objects.isNull(petDTO) ? true: false;
        if(errorDatos)
            throw new UnsupportedOperationException();

        if (petDTO.getName().isEmpty()  || petDTO.getNotes().isEmpty())
            throw new UnsupportedOperationException();

        Pet pet = DTOaPet(petDTO,"ownerId");
        Long id = mascotaService.guardar(pet);

        if (id<=0)
            return petDTO;

        //Entity save send response
        petDTO.setId(id);
        return petDTO;
    }

    @Autowired
    private PetResourceAssembler assembler;

    @GetMapping("/listPets")
    ResponseEntity < CollectionModel<EntityModel<Pet>> > list(){
        List<EntityModel<Pet> > resourcesPet = null;
        resourcesPet = mascotaService.mascotas().stream()
                    .map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(new CollectionModel<>( resourcesPet));
    }

    @GetMapping("/petEntity/{petId}")
    EntityModel<Pet> getPetEntity(@PathVariable long petId) {
        Pet pet = null;
        pet = mascotaService.mascotaxId(petId);
        if (Objects.isNull(pet))
            return null;

        return assembler.toModel(pet);
    }

    private JasperPrint generarRecord(List<Pet> pets){
        try{
            //Parámetros dinámicos necesarios para el informe
            Map<String, Object> petParams = new HashMap<String, Object>();
            petParams.put("CompanyName", "Tech-Wong");
            petParams.put("employeeData", new JRBeanCollectionDataSource(pets));

            JasperPrint petReport =
                    JasperFillManager.fillReport(
                            JasperCompileManager.compileReport(
                                    ResourceUtils.getFile("classpath:employees-details.jrxml")
                                            .getAbsolutePath())
                            , petParams //parametros dinamicos
                            , new JREmptyDataSource());

            return petReport;
        }catch (Exception ex){
            return null;
        }
    }

    @GetMapping("/records/report")
    public ResponseEntity<byte[]> getPetRecordReport(){
        List<Pet> pets = mascotaService.mascotas();

        if(pets.isEmpty()) //Return status 200 and data empty
            return new ResponseEntity<byte[]>(HttpStatus.OK);

        JasperPrint jasperPrint = generarRecord(pets);

        if(Objects.isNull(jasperPrint))
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);

        //Establecer configuracion del formato a PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename","pets-details.pdf");

        try {
            return new ResponseEntity<byte[]>(JasperExportManager.exportReportToPdf(jasperPrint),headers, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<byte[]> getPetsRecordReport(@RequestParam(required = false) int numberPet){
        try{
            CustomJasperReport report = mascotaService.generatePetReport(numberPet);

            //Establecer configuracion del formato a PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename",report.getOutPutFilename());


            return new ResponseEntity<byte[]>(responseReportPDF(report),headers,HttpStatus.OK );
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<PetDTO> getPets(){
        List<Pet> pets = mascotaService.mascotas();
        if(pets.isEmpty())
            return new ArrayList<PetDTO>(Collections.EMPTY_LIST);

        return pets.stream().map(p -> petaDTO(p,"ownerId")).collect(Collectors.toList());
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = null;
        pet = mascotaService.mascotaxId(petId);
        if (Objects.isNull(pet))
            return null;

        return petaDTO(pet);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = mascotaService.mascotasXCliente(ownerId);
        if(pets.isEmpty())
            return Collections.EMPTY_LIST;

        return pets.stream().map(p -> petaDTO(p)).collect(Collectors.toList());
    }
}
