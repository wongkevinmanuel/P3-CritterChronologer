package com.udacity.jdnd.course3.critter.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    public String extraerUserName(String token) {
        return "";
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInkey(){
        byte[] keyBytes = Decoders.BASE64.decode("${env.SECRET_KEY}");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
