package com.example.shiny_potato.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.shiny_potato.filters.JwtAuthenticationFilter;
import com.example.shiny_potato.utilities.JwtUtil;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Chiave segreta per firmare i JWT
    private static final String SECRET_KEY = "improved-rotary-phone";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        // CORS configurazione
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.addAllowedOrigin("http://localhost:4200"); // Permetti richieste dal frontend Angular
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            // Disabilita CSRF per le API REST
            .csrf(csrf -> csrf.disable())
            .headers(headers -> {
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin()); // Permetti gli iframe con la stessa origine
            })
            // Aggiungi il filtro JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // Autorizzazione per gli endpoint
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/login", "/auth/register", "/h2-console/**").permitAll() // Endpoint pubblici
                .requestMatchers("/events", "/venues").authenticated() // GET su /events e /venues sono accessibili a chi è autenticato
                .requestMatchers("/reviews", "/reviews/**").authenticated() // Tutti gli endpoint relativi a /reviews richiedono autenticazione
                .requestMatchers("/events/**", "/venues/**").hasAuthority("MANAGER") // Tutte le altre operazioni su /events e /venues (POST, PUT, DELETE) richiedono l'autorità MANAGER
                .anyRequest().authenticated() // Gli altri endpoint sono protetti
            )
            // Gestione sessione stateless (nessuna sessione)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Configura stateless per REST APIs

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] secretKeyBytes = SECRET_KEY.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build(); // Decodifica i JWT usando la chiave segreta
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa un encoder sicuro (BCrypt)
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil); // Inietta JwtUtil nel filtro
    }
}
