package com.uniquindio.alojamientosAPI;

import com.uniquindio.alojamientosAPI.domain.exception.ReservaSuperpuestaException;
import com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException;
import com.uniquindio.alojamientosAPI.domain.services.impl.ReservationServiceImpl;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.Collections;

class ReservationServiceImplTest {

    @Test
    void crearReservaFechasInvalidas_lanzaExcepcion() {
        ReservationServiceImpl service = Mockito.mock(ReservationServiceImpl.class);
        Mockito.when(service.createReservation(1L, 1L, null, null, Collections.emptyList()))
                .thenThrow(new IllegalArgumentException("Fechas inválidas"));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.createReservation(1L, 1L, null, null, Collections.emptyList()));
    }

    @Test
    void crearReservaSuperpuesta_lanzaExcepcion() {
        ReservationServiceImpl service = Mockito.mock(ReservationServiceImpl.class);
        Mockito.when(service.createReservation(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), Collections.emptyList()))
                .thenThrow(new ReservaSuperpuestaException("Ya existe una reserva confirmada en ese rango de fechas"));
        Assertions.assertThrows(ReservaSuperpuestaException.class, () ->
                service.createReservation(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2), Collections.emptyList()));
    }

    @Test
    void cancelarReservaUsuarioNoAutorizado_lanzaExcepcion() {
        ReservationServiceImpl service = Mockito.mock(ReservationServiceImpl.class);
        Mockito.when(service.cancelReservation(1L, 99L))
                .thenThrow(new UsuarioNoAutorizadoException("No tienes permiso para cancelar esta reserva"));
        Assertions.assertThrows(UsuarioNoAutorizadoException.class, () ->
                service.cancelReservation(1L, 99L));
    }
}
