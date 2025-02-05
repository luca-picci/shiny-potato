package com.example.shiny_potato.utilities;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.example.shiny_potato.repositories.UserRepository;
import com.example.shiny_potato.entitities.User;
import java.security.Key;
import java.util.Date;

/**
 * Classe di utilità per la generazione e la validazione di JWT (JSON Web Token).
 */
@Component
public class JwtUtil {

    private final Key key;
    private final UserRepository userRepository;

    /**
     * Costruttore per JwtUtil.
     * @param userRepository Il repository per accedere ai dati degli utenti.
     */
    public JwtUtil(UserRepository userRepository) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.userRepository = userRepository;
    }

    /**
     * Genera un JWT per l'utente fornito.
     * @param user L'utente per il quale viene generato il token.
     * @return Il JWT generato.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", "ROLE_" + user.getUserType())
                .claim("userId", user.getId()) // Includi l'ID utente nel token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Scadenza dopo 1 ora
                .signWith(key)
                .compact();
    }

    /**
     * Valida il JWT fornito.  Verifica la corrispondenza con i dati nel database.
     * @param token Il JWT da validare.
     * @return True se il token è valido e corrisponde ai dati nel DB, false altrimenti.
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            Long userId = claims.get("userId", Long.class); // ID utente dal token
            String email = claims.getSubject(); // Email dal token

            User user = userRepository.findById(userId).orElse(null); // Recupera l'utente dal DB *usando l'ID*

            if (user == null || !user.getEmail().equals(email)) { // Controllo base: utente esiste e email corrisponde
                return false; // Utente non trovato o email non corrispondente
            }

            return claims.getExpiration().after(new Date()); // Controllo scadenza (secondario, ma importante)

        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token non valido
        }
    }

    /**
     * Estrae l'email dal JWT fornito.
     * @param token Il JWT da cui estrarre l'email.
     * @return L'email estratta, o null se il token non è valido.
     */
    public String extractEmail(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null; // Gestisci il caso di token non valido
        }
    }

        /**
     * Estrae l'ID utente dal JWT fornito.
     * @param token Il JWT da cui estrarre l'ID utente.
     * @return L'ID utente estratto, o null se il token non è valido.
     */
    public Long extractUserId(String token) {
        try {
            return parseClaims(token).get("userId", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null; // Gestisci il caso di token non valido
        }
    }

    /**
     * Estrae il ruolo dal JWT fornito.
     * @param token Il JWT da cui estrarre il ruolo.
     * @return Il ruolo estratto, o null se il token non è valido.
     */
    public String extractRole(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.get("role", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null; // Gestisci il caso di token non valido
        }
    }


    /**
     * Analizza il JWT fornito e restituisce i Claims.
     * Questo metodo è privato e usato solo internamente.
     * @param token Il JWT da analizzare.
     * @return I Claims analizzati.
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}