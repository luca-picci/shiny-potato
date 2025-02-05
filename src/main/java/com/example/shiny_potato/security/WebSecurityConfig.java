package com.example.shiny_potato.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.shiny_potato.filters.JwtAuthenticationFilter;
import com.example.shiny_potato.utilities.JwtUtil;
import com.example.shiny_potato.services.MyUserDetailsService; 

/**
 * Classe di configurazione per Spring Security.
 * Definisce le regole di sicurezza per l'applicazione,
 * inclusi i percorsi consentiti, le autorizzazioni e il filtro JWT.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Configura la catena di filtri di sicurezza (SecurityFilterChain).
     *
     * @param http               L'oggetto HttpSecurity per la configurazione.
     * @param jwtAuthenticationFilter Il filtro di autenticazione JWT.
     * @param userDetailsService  Il servizio per caricare i dettagli dell'utente.
     * @return La catena di filtri di sicurezza configurata.
     * @throws Exception Se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, MyUserDetailsService userDetailsService) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:4200")); // Origini consentite (localhost:4200 per Angular)
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Metodi HTTP consentiti
                config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Intestazioni consentite
                config.setAllowCredentials(true); // Abilita l'invio di cookie e credenziali con le richieste CORS
                return config;
            }))
            .csrf(csrf -> csrf.disable()) // Disabilitato per API stateless (JWT)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/login", "/auth/register", "/h2-console/**").permitAll() // Permetti accesso a login, registrazione e H2 console
                .requestMatchers(HttpMethod.GET, "/events", "/events/**", "/venues", "/venues/**").authenticated() // Richiedi autenticazione per le GET su eventi/venues
                .requestMatchers("/events", "/events/**", "/venues", "/venues/**").hasAuthority("ROLE_MANAGER") // Richiedi ruolo MANAGER per altre operazioni su eventi/venues
                .anyRequest().authenticated() // Tutte le altre richieste richiedono autenticazione
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sessioni stateless (JWT)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Aggiungi il filtro JWT prima del filtro standard di autenticazione

        return http.build();
    }

    /**
     * Crea un bean per il PasswordEncoder.
     * Utilizzato per codificare le password prima di salvarle nel database.
     *
     * @return Il PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder è un buon algoritmo di hashing
    }

    /**
     * Crea un bean per il filtro di autenticazione JWT.
     *
     * @param jwtUtil           L'utility per la gestione dei JWT.
     * @param userDetailsService Il servizio per caricare i dettagli dell'utente.
     * @return Il filtro di autenticazione JWT.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, MyUserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }
}
