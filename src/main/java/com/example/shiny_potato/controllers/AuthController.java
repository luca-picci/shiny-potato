package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shiny_potato.entities.User;
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO userDTO) {
        System.out.println("🔑 Tentativo di login con email: " + userDTO.getEmail());

        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            System.out.println("⛔ Password mancante");
            throw new IllegalArgumentException("Password cannot be empty");
        }
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        
        if (userOptional.isEmpty()) {
            System.out.println("⛔ Utente non trovato!");
            response.put("message", "Credenziali non valide");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        User userFromDb = userOptional.get();
        System.out.println("✅ Utente trovato: " + userFromDb.getEmail());

        boolean passwordMatches = passwordEncoder.matches(userDTO.getPassword(), userFromDb.getPassword());
        System.out.println("🔍 Password corretta? " + passwordMatches);

        System.out.println("🔑 Password inserita dall'utente: " + userDTO.getPassword());
        System.out.println("🔒 Password memorizzata nel database: " + userFromDb.getPassword());
        System.out.println("✅ Confronto password: " + passwordEncoder.matches(userDTO.getPassword(), userFromDb.getPassword()));


        if (!passwordMatches) {
            response.put("message", "Credenziali non valide");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = jwtUtil.generateToken(userFromDb);
        System.out.println("✅ Token generato: " + token);

        response.put("token", token);
        response.put("userId", userFromDb.getId());
        response.put("role", userFromDb.getUserType());
        
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