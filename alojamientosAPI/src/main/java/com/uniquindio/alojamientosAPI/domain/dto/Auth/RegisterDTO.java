package com.uniquindio.alojamientosAPI.domain.dto.Auth;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String firstName,
    
    @NotBlank(message = "El apellido es obligatorio")
   
    String lastName,
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    LocalDate dayOfBirth,
    
    @NotBlank(message = "El número de teléfono es obligatorio")
    String phoneNumber,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    String password,
    
    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    String homeAddress
) {
}