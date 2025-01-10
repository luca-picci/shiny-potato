package com.example.shiny_potato.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shiny_potato.entitities.User;
import com.example.shiny_potato.repositories.UserRepository;
import com.example.shiny_potato.utilities.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Login
    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail());
        if(user != null && user.getPassword().equals(loginUser.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        }
        return "Invalid credentials";
    }

    // Registration
    @PostMapping("/register")
    public String register(@RequestBody User newUser) {
        userRepository.save(newUser);
        return "User registrated successfully";
    }
}
