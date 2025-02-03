package com.example.shiny_potato.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        // Estrai il token JWT dall'header Authorization
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Rimuovi il prefisso "Bearer "

            // Estrai l'email dal token
            String email = jwtUtil.extractEmail(token);
            if (email != null) {
                // Ora estrai anche l'ID dell'utente
                Long userId = jwtUtil.extractUserId(token);
                // Usa il metodo validateToken che accetta tre parametri
                if (jwtUtil.validateToken(token, email, userId)) {
                    // Per sicurezza, estrai il claim "role" (assumendo che sia una stringa)
                    String role = jwtUtil.extractClaim(token, "role");
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    
                    // Imposta l'autenticazione nel contesto di sicurezza di Spring
                    SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(email, null, authorities)
                    );
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
