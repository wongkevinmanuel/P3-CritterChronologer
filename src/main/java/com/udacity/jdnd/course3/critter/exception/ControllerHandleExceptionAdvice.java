package com.udacity.jdnd.course3.critter.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ControllerHandleExceptionAdvice extends ResponseEntityExceptionHandler {
    Log log = LogFactory.getLog(this.getClass());

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex);
        ex.getBindingResult();
        return handleExceptionInternal(ex,"",headers,HttpStatus.BAD_REQUEST,request);
        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
