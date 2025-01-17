package com.example.shiny_potato.utilities;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Genera il token includendo l'email, il ruolo e l'ID dell'utente
    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)  // Aggiungi il ruolo nel claim del token
            .claim("userId", userId)  // Aggiungi l'ID dell'utente nel claim
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Setta la scadenza del token
            .signWith(key)
            .compact();
    }

    // Estrai l'email dal token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    // Estrai l'ID dell'utente dal token
    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId", String.class));  // Restituisce l'ID dell'utente come Long
    }

    // Verifica se il token è scaduto
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }

    // Verifica la validità del token
    public boolean validateToken(String token, String email, Long userId) {
        return (email.equals(extractEmail(token)) 
                && userId.equals(extractUserId(token))  // Verifica che l'ID dell'utente nel token corrisponda
                && !isTokenExpired(token));
    }

    // Estrai un claim specifico dal token
    public String extractClaim(String token, String claim) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(claim, String.class);  // Restituisce il claim specificato
    }
}

