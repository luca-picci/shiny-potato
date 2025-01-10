package com.example.shiny_potato.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/venues/**").hasRole("MANAGER") // Solo i manager possono accedere agli endpoint di Venue
                .anyRequest().authenticated() // Gli altri endpoint richiedono autenticazione
            )
            .httpBasic(); // Abilita l'autenticazione di base (basic auth)
            

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails client = User.withUsername("user")
            .password(passwordEncoder.encode("password"))
            .roles("CLIENT")
            .build();

        UserDetails manager = User.withUsername("admin")
            .password(passwordEncoder.encode("password"))
            .roles("MANAGER")
            .build();

        return new InMemoryUserDetailsManager(client, manager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
