package com.uniquindio.alojamientosAPI.config;

import com.uniquindio.alojamientosAPI.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
            // Deshabilitar CSRF (no necesario para APIs stateless con JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar política de sesiones como STATELESS (sin sesiones)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // ✅ Endpoints públicos de autenticación (sin token)
                .requestMatchers("/api/auth/**").permitAll()
                
                // ✅ Endpoints públicos generales
                .requestMatchers("/api/public/**", "/public/**").permitAll()
                
                // ✅ Swagger/OpenAPI (opcional, para documentación)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ✅ Endpoints protegidos por roles
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/host/**").hasRole("ANFITRION")
                .requestMatchers("/api/client/**").hasRole("CLIENTE")

                // ✅ Rutas que pueden acceder cualquiera de los tres roles
                .requestMatchers("/api/private/**").hasAnyRole("CLIENTE", "ANFITRION", "ADMINISTRADOR")

                // ✅ Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            
            // Agregar el filtro JWT antes del filtro de autenticación de Spring
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
