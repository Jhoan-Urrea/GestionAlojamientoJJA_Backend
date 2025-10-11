package com.uniquindio.alojamientosAPI.web;

import com.uniquindio.alojamientosAPI.domain.exception.EntidadNoEncontradaException;
import com.uniquindio.alojamientosAPI.domain.exception.ReservaSuperpuestaException;
import com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<String> handleEntidadNoEncontrada(EntidadNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ReservaSuperpuestaException.class)
    public ResponseEntity<String> handleReservaSuperpuesta(ReservaSuperpuestaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoAutorizadoException.class)
    public ResponseEntity<String> handleUsuarioNoAutorizado(UsuarioNoAutorizadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

