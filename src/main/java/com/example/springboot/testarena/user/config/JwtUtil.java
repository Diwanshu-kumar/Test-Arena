package com.example.springboot.testarena.user.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Generate a secure key for signing and parsing JWTs
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 864_000_00; // 1 day in milliseconds

    // Generate a JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        Claims claims = getClaims(token);
        if(claims == null)return "";
        return claims.getSubject();
    }

    // Validate the JWT token
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = getClaims(token);
            if(claims!=null) {
                return (username.equals(claims.getSubject()) && !isTokenExpired(token));
            }
        } catch (Exception e) {
            // Handle exceptions related to token parsing or validation
            return false;
        }
        return false;
    }

    // Check if the JWT token has expired
    public boolean isTokenExpired(String token) {
        Claims claims = getClaims(token);
        if(claims==null)return true;
        return claims.getExpiration().before(new Date());
    }

    // Get Claims from the JWT token
    private Claims getClaims(String token) {
        try {

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build();
            return parser.parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException | SignatureException e){
            return null;
        }
    }
}
