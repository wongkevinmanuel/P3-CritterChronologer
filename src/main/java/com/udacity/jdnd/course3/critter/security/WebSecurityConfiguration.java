package com.udacity.jdnd.course3.critter.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Clase de configuracion
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    public WebSecurityConfiguration(BCryptPasswordEncoder b, UserDetailsServiceImpl u
    ){
       this.userDetailsService  = u;
       this.bCryptPasswordEncoder = b;
    }

    //Define los recursos públicos. A continuación, hemos establecido el
    // punto final SIGN_UP_URL como público. El http.cors() se utiliza para
    // hacer que Spring Security admita CORS (Cross-Origin Resource Sharing)
    // y CSRF (Cross-Site Request Forgery).
    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtAuthFilter;
        AuthenticationProvider authenticationProvider;
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                //.antMatchers(HttpMethod.POST, JWTPersonalSecurityConstants.SIGN_UP_URL).permitAll()
                .requestMatchers("")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                //Politica de creacion de session (Sin Estado)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Declara BCryptPasswordEncoder como la técnica de codificación
    //y carga datos específicos del usuario.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.parentAuthenticationManager(authenticationManagerBean())
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
}
