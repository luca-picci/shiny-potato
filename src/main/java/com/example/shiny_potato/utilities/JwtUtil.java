package com.example.shiny_potato.utilities;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.example.shiny_potato.repositories.UserRepository;
import com.example.shiny_potato.entitities.User;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.userRepository = userRepository;
    }

    // Genera il token basato sull'utente
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getUserType())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 ora
                .signWith(key)
                .compact();
    }

    // Overload di validateToken che accetta solo il token (estrae email e userId)
    public boolean validateToken(String token) {
        try {
            String email = extractEmail(token);
            Long userId = extractUserId(token);
            return validateToken(token, email, userId);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Validazione completa: verifica email, userId e scadenza
    public boolean validateToken(String token, String email, Long userId) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long extractedUserId = claims.get("userId", Long.class);
            User user = userRepository.findById(extractedUserId)
                    .orElseThrow(() -> new JwtException("Utente non trovato"));

            return claims.getSubject().equals(user.getEmail()) &&
                   claims.get("role").equals(user.getUserType()) &&
                   !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Estrai l'email dal token
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // Estrai l'ID dell'utente dal token
    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Estrai un claim specifico dal token
    public String extractClaim(String token, String claim) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(claim, String.class);
    }
}
