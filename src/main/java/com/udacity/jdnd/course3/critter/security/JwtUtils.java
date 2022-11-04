package com.udacity.jdnd.course3.critter.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Proporciona métodos para generar, analizar y validar JWT
 */
//@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${wong.app.jwtSecret}")
    private String jwtSecret;

    @Value("${wong.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${wong.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null)
            return cookie.getValue();
        else
            return null;
    }

}
