package com.uniquindio.alojamientosAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Endpoints públicos sin autenticación
                .requestMatchers("/api/public/**", "/public/**").permitAll()

                // ✅ Endpoints protegidos por roles
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/host/**").hasRole("ANFITRION")
                .requestMatchers("/api/client/**").hasRole("CLIENTE")

                // ✅ Rutas que pueden acceder cualquiera de los tres roles
                .requestMatchers("/api/private/**").hasAnyRole("CLIENTE", "ANFITRION", "ADMINISTRADOR")

                // ✅ Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            // Autenticación básica temporal
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}
