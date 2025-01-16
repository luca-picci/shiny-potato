package com.example.shiny_potato.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;  // PasswordEncoder è iniettato

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginUser) {
        // Cerca l'utente nel database per email
        User userFromDb = userRepository.findByEmail(loginUser.getEmail());
        Map<String, String> response = new HashMap<>();
        
        if (userFromDb != null) {
            // Verifica se la password fornita corrisponde a quella criptata nel database
            if (passwordEncoder.matches(loginUser.getPassword(), userFromDb.getPassword())) {
                // Ottieni il ruolo dell'utente, qui assumiamo che esista un campo `userType` nell'entità `User`
                String role = userFromDb.getUserType().toString();  // Converte l'enum in stringa

                // Genera il token JWT con l'email e il ruolo dell'utente
                String token = jwtUtil.generateToken(userFromDb.getEmail(), role);
                response.put("token", token);
                return ResponseEntity.ok(response);  // Restituisce il token JWT con una risposta 200
            } else {
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 401 Unauthorized
            }
        }
        
        // Se l'utente non esiste
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // 401 Unauthorized
    }

    // Registro utente (facoltativo)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User newUser) {
        // Crea un nuovo utente con password criptata
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // Cripta la password prima di salvarla
        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
}
