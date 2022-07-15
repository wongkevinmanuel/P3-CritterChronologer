package com.udacity.jdnd.course3.critter.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerHandleExceptionAdvice extends ResponseEntityExceptionHandler {
    Log log = LogFactory.getLog(this.getClass());


}
