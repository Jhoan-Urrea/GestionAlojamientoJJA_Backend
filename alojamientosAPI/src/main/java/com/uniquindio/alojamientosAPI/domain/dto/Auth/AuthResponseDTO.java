package com.uniquindio.alojamientosAPI.domain.dto.Auth;

import java.util.Set;

public record AuthResponseDTO(
    String token,
    String email,
    String firstName,
    String lastName,
    Set<String> roles
) {
}
