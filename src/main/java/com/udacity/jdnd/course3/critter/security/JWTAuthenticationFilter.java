package com.udacity.jdnd.course3.critter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.jdnd.course3.critter.user.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//Encargada de procesar la autenticacion.
//La clase base analiza las credenciales
//de usuario(username y password)
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    public  JWTAuthenticationFilter(AuthenticationManager authManager){
        this.authenticationManager = authManager;
    }

    //Realiza la authenticacion mediantes el analisis
    //de credenciales de usuario
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res){
        User credentials = null;
        try {
            credentials = new ObjectMapper().readValue(req.getInputStream(), User.class);
        }catch (IOException ioException){
            logger.error("Error - IOException on auth.",ioException);
            throw new RuntimeException(ioException);
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUserName(),
                        credentials.getPassword(),
                        new ArrayList<>()
                )
        );
    }
}
