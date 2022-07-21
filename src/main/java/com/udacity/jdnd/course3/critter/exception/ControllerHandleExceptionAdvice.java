package com.udacity.jdnd.course3.critter.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        body.put("message-user","Malformed JSON request");
        log.error(ex.getCause());

        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    //Manage when the record no found.
    @ExceptionHandler({EmptyResultDataAccessException.class})
    protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", ex.getCause() != null ? ex.getCause().getMessage() : ex.toString());
        body.put("message-user","Empty result data (Record not found).");
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
}
