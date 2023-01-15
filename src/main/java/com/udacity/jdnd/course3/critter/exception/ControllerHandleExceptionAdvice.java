package com.udacity.jdnd.course3.critter.exception;

import com.udacity.jdnd.course3.critter.pet.service.PetNotFoundException;
import javassist.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerHandleExceptionAdvice extends ResponseEntityExceptionHandler {
    Log log = LogFactory.getLog(this.getClass());

    //Send message for invalid field value
    // example: @Value field with @Size(min=3) in DTO
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatus status
            , WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", status.value());
        List<String> errores = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("error", errores);
        log.error(errores);

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    // Send message for Invalid requisition( eg: invalid fiedl) message.
    // Example: try send request, with body Empty for view message error.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers
            ,HttpStatus status, WebRequest request){

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", status.value());
        String error = ex.getCause() !=null ? ex.getCause().getMessage(): ex.toString();
        body.put("error",error);
        body.put("messageuser","Malformed JSON request");
        log.error(ex);

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    //Manage when the record no found.
    @ExceptionHandler({EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", ex.getCause() != null ? ex.getCause().getMessage() : ex.toString());
        String massage_user = "Empty result data (Record not found).";
        body.put("messageuser", massage_user);
        log.error( ex + " message-user: "+ massage_user);

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    //NT
    //Thrown to indicate that a method has been passed an illegal or inappropriate argument.
    //Example try the method
    @ExceptionHandler({IllegalArgumentException.class, NotFoundException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        String titularContextoLocal = "parameters.invalid "+LocaleContextHolder.getLocale().toString();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        String error = titularContextoLocal + " || " + ex.getCause() != null ?
                        ex.getCause().getMessage() : ex.toString();
        body.put("error", error);
        String message_user= "Passed an illegal or inappropriate argument.";
        body.put("messageuser",message_user);
        log.error(ex + " massage-user: " + message_user );

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PetNotFoundException.class})
    public ResponseEntity<Object> handlePetNotFoundException(PetNotFoundException ex
                                                            , WebRequest request){
        Map<String, Object> body = new LinkedHashMap<>();
        //String titularContextoLocal = ".k "+ LocaleContextHolder.getLocale().toString();
        String error = ex.getCause() == null ? ex.toString() : ex.getCause().getMessage();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", error);
        String message_user = "Error accessing the DB by the pet service.";
        body.put("messageuser", message_user);
        log.error(ex + " message-user:" +message_user);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //Maneja problemas en la persistencia de datos
    //Tiene diferente causas
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,WebRequest request){
        Map<String, Object> body = new LinkedHashMap<>();
        //String titularContextoLocal = ".k "+ LocaleContextHolder.getLocale().toString();
        String error = ex.getCause() == null ? ex.toString() : ex.getCause().getMessage();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", error);
        String message_user = "Possible errors: 1) Violation of the DB structure (verify relations between tables). 2) Some property contains an error (Does not allow null values or the expected value is not correct)";
        body.put("messageuser", message_user);
        log.error(ex + " message-user:" +message_user);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException ex, WebRequest request){
        Map<String, Object > body = new LinkedHashMap<>();
        String error = "";
        if(ex.getCause() != null ) {
            error = ex.toString() + " ==>> Cause: " + ex.getCause().getMessage();
        }
        body.put("timestamp", LocalDate.now());
        body.put("status",HttpStatus.BAD_REQUEST);
        body.put("error", error);body.put("messageuser","Error internal Server.");
        log.error(ex + " message-user:"+" Error internal Server.");
        return new ResponseEntity<>(body ,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
