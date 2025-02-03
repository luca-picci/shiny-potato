package com.example.shiny_potato.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shiny_potato.entitities.User;
import com.example.shiny_potato.repositories.UserRepository;
import com.example.shiny_potato.utilities.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection via constructor
    public AuthController(UserRepository userRepository, 
                         JwtUtil jwtUtil,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginUser) {
        Map<String, Object> response = new HashMap<>();
        
        // 1. Cerca l'utente per email (usa Optional per gestire il caso null)
        Optional<User> userOptional = userRepository.findByEmail(loginUser.getEmail());
        
        if (userOptional.isEmpty()) {
            response.put("message", "Credenziali non valide");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        User userFromDb = userOptional.get();

        // 2. Verifica password
        if (!passwordEncoder.matches(loginUser.getPassword(), userFromDb.getPassword())) {
            response.put("message", "Credenziali non valide");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 3. Genera token usando la nuova versione di JwtUtil
        String token = jwtUtil.generateToken(userFromDb);
        
        // 4. Prepara risposta
        response.put("token", token);
        response.put("userId", userFromDb.getId());
        response.put("role", userFromDb.getUserType());  // Assicurati che il campo si chiami "role" nell'entità
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User newUser) {
        Map<String, String> response = new HashMap<>();
        
        // Verifica se l'email è già registrata
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            response.put("message", "Email già registrata");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Cripta la password e salva
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        
        response.put("message", "Registrazione completata con successo");
        return ResponseEntity.ok(response);
    }
}