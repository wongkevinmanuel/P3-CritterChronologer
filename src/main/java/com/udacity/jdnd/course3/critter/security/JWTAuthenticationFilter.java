package com.udacity.jdnd.course3.critter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Encargada de configurar el filtro.
//La clase base extiende OncePerRequestFilter
// (Seria Una filtro por cada solicitud)
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    /*private AuthenticationManager authenticationManager;
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
    }*/

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter() {
    }

    private boolean authHeaderNUllorNotBearer(String header){
        return header == null || !header.startsWith("Bearer ") ? true : false;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        //Realizar el proceso de filtrar la solicitud
        //Cuando hacemos una llamada necesitamos pasar el JWT (token autenticacion)
        //dentro del header (encabezado) procesar el encabezado
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeaderNUllorNotBearer(authHeader)){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extraerUserName(jwt);
        //Verificar si el usuario esta autenticado
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                   userDetails, null, userDetails.getAuthorities()
                );
                //Configurar detalles del token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

    }

}
