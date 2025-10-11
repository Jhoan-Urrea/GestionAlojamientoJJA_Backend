package com.uniquindio.alojamientosAPI.domain.exception;

public class UsuarioNoAutorizadoException extends RuntimeException {
    public UsuarioNoAutorizadoException(String message) {
        super(message);
    }
}

