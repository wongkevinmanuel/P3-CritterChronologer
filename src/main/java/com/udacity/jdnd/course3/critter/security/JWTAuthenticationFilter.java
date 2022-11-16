package com.udacity.jdnd.course3.critter.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.jdnd.course3.critter.login.requests.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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

    //Es presente en el padre de la clase base.
    //Depues de anular, se llamara al metodo
    //despues que usuario inicie session correctamente.
    //Genera un Token de cadena JWT para este usuario
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication)
        throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(
                        ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()
                ).withExpiresAt(
                        new Date(System.currentTimeMillis() + JWTPersonalSecurityConstants.EXPIRATION_TIME)
                ).sign(
                        Algorithm.HMAC512(JWTPersonalSecurityConstants.SECRET.getBytes()));
        response.addHeader(JWTPersonalSecurityConstants.HEADER_STRING
                ,JWTPersonalSecurityConstants.TOKEN_PREFIX + token);
    }

}
