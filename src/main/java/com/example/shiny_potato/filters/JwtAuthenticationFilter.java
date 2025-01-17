package com.example.shiny_potato.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importa la classe
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.shiny_potato.utilities.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // Permetti il passaggio per le route di login e registrazione
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrai il token JWT
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Rimuovi il prefisso "Bearer "

            // Estrai l'email e il ruolo dal token
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractClaim(token, "role");
            Long userId = jwtUtil.extractUserId(token);

            // Verifica che il token sia valido
            if (email != null && jwtUtil.validateToken(token, email, userId)) {
                // Aggiungi l'autorizzazione per il ruolo dell'utente
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                // Imposta l'autenticazione nel contesto di sicurezza di Spring
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(email, null, authorities)
                );
            }
        }
        
        // Continua la catena dei filtri
        filterChain.doFilter(request, response);
    }
}
