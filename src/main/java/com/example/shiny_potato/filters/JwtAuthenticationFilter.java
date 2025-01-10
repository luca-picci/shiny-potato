package com.example.shiny_potato.filters;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.shiny_potato.utilities.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Estrai il token dall'header "Authorization"
        String token = request.getHeader("Authorization");

        // Verifica che il token sia presente e inizi con "Bearer "
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Rimuovi il prefisso "Bearer "

            // Estrai l'email dal token e valida il token
            String email = jwtUtil.extractEmail(token);
            if (email != null && jwtUtil.validateToken(token, email)) {
                // Autenticazione con UsernamePasswordAuthenticationToken
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>())
                );
            }
        }

        // Continua con la catena di filtri
        filterChain.doFilter(request, response);
    }
}
