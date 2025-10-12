package com.uniquindio.alojamientosAPI.domain.service.auth.interfaces;

import com.uniquindio.alojamientosAPI.domain.dto.Auth.AuthResponseDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.LoginDTO;
import com.uniquindio.alojamientosAPI.domain.dto.Auth.RegisterDTO;

public interface AuthService {

    /**
     * Metodo encargado de registrar al usuario.
     * @param registerDTO
     * @return
     */
    AuthResponseDTO register(RegisterDTO registerDTO);

    /**
     * Metodo encargado de Login del usuario.
     * @param loginDTO
     * @return
     */
    AuthResponseDTO login(LoginDTO loginDTO);
    
}