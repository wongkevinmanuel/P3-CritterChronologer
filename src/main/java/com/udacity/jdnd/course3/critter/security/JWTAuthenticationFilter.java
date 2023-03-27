package com.udacity.jdnd.course3.critter.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/***
 * Se seleccion de forma predeterminada por spring
 * para colocar los filtros.
 * Se encadenan en cada validacion por lo q son necesarios
 * para spring boot. Se deben implementar los filtros y
 * proporcionar los metodos necesarios para la
 * implementacion JWT.
 */
public class JWTAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationM){
        this.authenticationManager = authenticationM;
    }

    /***
     * Intento de autenticacion
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(),User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch (IOException e){
            throw new RuntimeException(e);//return super.attemptAuthentication(request, response);
        }
    }

    /***
     * Autenticacion exitosa
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTPersonalSecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(JWTPersonalSecurityConstants.SECRET.getBytes()));
        response.addHeader(JWTPersonalSecurityConstants.HEADER_STRING, JWTPersonalSecurityConstants.TOKEN_PREFIX + token);
    }
}
