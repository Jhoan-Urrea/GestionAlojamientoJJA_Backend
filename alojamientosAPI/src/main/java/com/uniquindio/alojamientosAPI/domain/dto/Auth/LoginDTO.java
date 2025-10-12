package com.uniquindio.alojamientosAPI.domain.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "El email es obligatorio") 
        @Email(message = "El formato del email no es válido") 
        String email,
        
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}