package com.tamscrap.config;

import java.security.Key; // Importamos Key desde java.security
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "your-256-bit-secret-key-must-be-long-enough-to-work";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());  

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    } 

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key) 
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

     
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 172800000)) // Token válido por 2 días
                .signWith(key)
                .compact();
    }

}
