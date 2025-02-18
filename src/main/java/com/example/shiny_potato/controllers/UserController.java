package com.example.shiny_potato.controllers;

import com.example.shiny_potato.entities.User;
import com.example.shiny_potato.services.UserService;
import com.example.shiny_potato.utilities.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint di login sicuro
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.email(), request.password());
        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }

    // Endpoint protetto
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Altri endpoint con validazione
    @GetMapping("/")
    public List<User> getAllUsers(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validateToken(token)) throw new SecurityException("Accesso negato");
        return userService.getAllUsers();
    }

    // Record per la richiesta di login
    public record LoginRequest(String email, String password) {}
}