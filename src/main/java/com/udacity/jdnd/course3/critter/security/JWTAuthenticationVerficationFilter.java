package com.udacity.jdnd.course3.critter.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.JWT;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/***
 * Archivo necesario par escanear y aplicar los filtros de jwt
 *
 */
@Component
public class JWTAuthenticationVerficationFilter
        extends BasicAuthenticationFilter {

    public JWTAuthenticationVerficationFilter(AuthenticationManager authenticationManager){
        super( authenticationManager);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest   request){
        String token = request.getHeader(JWTPersonalSecurityConstants.HEADER_STRING);

        if(token!= null){
            String user = JWT.require(HMAC512(JWTPersonalSecurityConstants.SECRET.getBytes()))
                    .build().verify(token.replace(JWTPersonalSecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null){
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    @Override
    public void doFilterInternal(HttpServletRequest  req,HttpServletResponse res
                                , FilterChain cadena) throws IOException, ServletException {
        String header = req.getHeader(JWTPersonalSecurityConstants.HEADER_STRING);

        if(header == null || !header.startsWith(JWTPersonalSecurityConstants.TOKEN_PREFIX)){
            cadena.doFilter(req,res);
            return;
            //Porque retornar en un metodo void????
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        cadena.doFilter(req,res);
    }
}
