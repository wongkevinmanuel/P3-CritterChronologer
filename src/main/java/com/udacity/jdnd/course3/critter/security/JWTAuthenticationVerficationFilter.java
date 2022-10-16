package com.udacity.jdnd.course3.critter.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import com.auth0.jwt.JWT;

public class JWTAuthenticationVerficationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationVerficationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //Valida el token leido del encabezado de autorizacion
    private UsernamePasswordAuthenticationToken getAuthenticationToken(
            HttpServletRequest request){
        String token = request.getHeader(JWTPersonalSecurityConstants.HEADER_STRING);
        if(!Objects.isNull(token)){
            //String user = JWT
        }

        return null;
    }

}
