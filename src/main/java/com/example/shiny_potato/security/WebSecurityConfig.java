package com.example.shiny_potato.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.shiny_potato.filters.JwtAuthenticationFilter;

import org.springframework.lang.NonNull;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {  // Usa WebMvcConfigurer, non un record

    // Metodo per configurare la sicurezza del backend
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/venues/**").hasRole("MANAGER") // Solo i manager possono accedere agli endpoint di Venue
                .requestMatchers(HttpMethod.POST, "/events").hasRole("MANAGER") // Solo i manager possono fare una POST su /events (Creazione evento)
                .requestMatchers(HttpMethod.PUT, "/events/**").hasRole("MANAGER") // Solo i manager possono fare una PUT su /events/{id} (Modifica evento)
                .requestMatchers(HttpMethod.DELETE, "/events/**").hasRole("MANAGER") // Solo i manager possono fare una DELETE su /events/{id} (Eliminazione evento)
                .anyRequest().authenticated() // Gli altri endpoint richiedono autenticazione
            );

        return http.build();
    }

    // Configurazione CORS
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {  // Aggiungi l'annotazione @NonNull
        // Permetti CORS solo per il frontend su localhost:4200 (porta di default di Angular)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")  // Cambia con l'URL del tuo frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // I metodi consentiti
                .allowedHeaders("*");  // Permetti tutti gli header
    }

    // In-memory user details manager per la gestione degli utenti
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails client = User.withUsername("user@example.com")
            .password(passwordEncoder.encode("password"))
            .roles("CLIENT")
            .build();

        UserDetails manager = User.withUsername("admin@example.com")
            .password(passwordEncoder.encode("password"))
            .roles("MANAGER")
            .build();

        return new InMemoryUserDetailsManager(client, manager);
    }

    // Encoder della password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
