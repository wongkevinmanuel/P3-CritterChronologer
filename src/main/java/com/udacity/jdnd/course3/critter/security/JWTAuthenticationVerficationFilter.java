package com.udacity.jdnd.course3.critter.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import com.auth0.jwt.JWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationVerficationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationVerficationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //Valida el token leido del encabezado de autorizacion
    private UsernamePasswordAuthenticationToken getAuthenticationToken(
            HttpServletRequest request){
        String token = request.getHeader(JWTPersonalSecurityConstants.HEADER_STRING);
        if(!Objects.isNull(token)){
            String user = JWT.require(HMAC512(JWTPersonalSecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(JWTPersonalSecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if(user!= null){
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    //Este metodo se usa cuando se tiene varios roles
    //y una politica para RBAC.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                                    , FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(JWTPersonalSecurityConstants.HEADER_STRING);
        if (header == null || !header.startsWith(JWTPersonalSecurityConstants.TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthenticationToken(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
