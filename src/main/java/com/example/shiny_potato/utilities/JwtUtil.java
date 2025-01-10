package com.example.shiny_potato.utilities;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;

public class JwtUtil {

     private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Modificato per usare l'email dell'utente
    public String generateToken(String email) {
        return Jwts.builder()
            .setSubject(email)  // Usa l'email come subject
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 ora di validità
            .signWith(key)
            .compact();
    }

    // Modificato per estrarre l'email dal token
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Restituisce l'email
        } catch (JwtException e) {
            // Token non valido o errore nel parsing
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        } catch (JwtException e) {
            // In caso di errore nel parsing o token invalido
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token, String email) {
        try {
            return (email.equals(extractEmail(token)) && !isTokenExpired(token));
        } catch (IllegalArgumentException e) {
            return false; // Se il token non è valido, non è da considerare valido
        }
    }

}

