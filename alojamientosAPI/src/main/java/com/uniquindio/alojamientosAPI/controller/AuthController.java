package com.uniquindio.alojamientosAPI.controller;

import com.uniquindio.alojamientosAPI.domain.dto.Auth.AuthResponseDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.RegisterDTO;
import com.uniquindio.alojamientosAPI.domain.service.auth.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario
     * @param registerDTO Datos del usuario a registrar
     * @return Token JWT y datos del usuario
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            AuthResponseDTO response = authService.register(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Email duplicado u otro error de validación
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para iniciar sesión
     * @param loginDTO Credenciales del usuario
     * @return Token JWT y datos del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            AuthResponseDTO response = authService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
