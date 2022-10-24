package com.udacity.jdnd.course3.critter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/***
 *
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    //private static final Logger = LogerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest Request
            , HttpServletResponse Response
            , AuthenticationException e) throws IOException, ServletException {
        //logger.error("Unauthorized error:{}", e.getMessage());
        Response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", e.getMessage());
        body.put("path", Request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Response.getOutputStream(), body);
    }
}
