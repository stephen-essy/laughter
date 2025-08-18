package com.laughter.laughter.Security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

    @Value("${app.secret.key}")
    private String secretString;

    private final long expirationMs=36000;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+expirationMs))
        .signWith(SignatureAlgorithm.HS512,secretString)
        .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()
        .setSigningKey(secretString)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    public boolean validateToken(String token,UserDetails userDetails){
        String email=extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
        .setSigningKey(secretString)
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
        return expiration.before(new Date());
    }
}