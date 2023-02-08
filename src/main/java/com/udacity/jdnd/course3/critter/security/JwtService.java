package com.udacity.jdnd.course3.critter.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public String extraerUserName(String token) {
        String claimsUser = extractClaim(token, Claims::getSubject);
        return claimsUser;
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //24 horas y 1000 milesegundos
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extraerUserName(token);
        boolean tokenValid = userName.equals(userDetails.getUsername()) && !isTokenExpired(token) ? true: false;
        return tokenValid;
    }

    private boolean isTokenExpired(String token) {
        return extracExpiration(token).before(new Date());
    }

    private Date extracExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInkey(){
        byte[] keyBytes = Decoders.BASE64.decode("${env.SECRET_KEY}");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
