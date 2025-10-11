package com.uniquindio.alojamientosAPI.domain.services.impl;

import com.uniquindio.alojamientosAPI.domain.services.ReservationService;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import com.uniquindio.alojamientosAPI.persistence.entity.StateReservation;
import com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.StateReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationGuestRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.AccommodationRepository;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import com.uniquindio.alojamientosAPI.domain.exception.ReservaSuperpuestaException;
import com.uniquindio.alojamientosAPI.domain.exception.UsuarioNoAutorizadoException;
import com.uniquindio.alojamientosAPI.domain.exception.EntidadNoEncontradaException;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private StateReservationRepository stateReservationRepository;
    @Autowired
    private ReservationGuestRepository reservationGuestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Override
    @Transactional
    public Reservation createReservation(Long userId, Long accommodationId, LocalDate startDate, LocalDate endDate, java.util.List<Long> guestIds) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));
        var accommodation = accommodationRepository.findById(accommodationId).orElseThrow(() -> new EntidadNoEncontradaException("Alojamiento no encontrado"));
        // Validar fechas
        if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Fechas inválidas");
        }
        // Validar disponibilidad (evitar reservas superpuestas)
        java.util.List<Reservation> reservasExistentes = reservationRepository.findAll();
        for (Reservation r : reservasExistentes) {
            if (r.getAccommodation().getId().equals(accommodationId) &&
                r.getState().getName().equalsIgnoreCase("CONFIRMADA") &&
                !(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate()))) {
                throw new ReservaSuperpuestaException("Ya existe una reserva confirmada en ese rango de fechas");
            }
        }
        StateReservation estadoPendiente = stateReservationRepository.findAll().stream()
            .filter(e -> e.getName().equalsIgnoreCase("PENDIENTE")).findFirst().orElseThrow(() -> new EntidadNoEncontradaException("Estado PENDIENTE no encontrado"));
        Reservation reserva = new Reservation();
        reserva.setUser(user);
        reserva.setAccommodation(accommodation);
        reserva.setStartDate(startDate);
        reserva.setEndDate(endDate);
        reserva.setState(estadoPendiente);
        reserva = reservationRepository.save(reserva);
        // Agregar invitados
        for (Long invitadoId : guestIds) {
            UserEntity invitado = userRepository.findById(invitadoId).orElseThrow(() -> new EntidadNoEncontradaException("Invitado no encontrado"));
            ReservationGuest rg = new ReservationGuest();
            rg.setReservation(reserva);
            rg.setGuest(invitado);
            reservationGuestRepository.save(rg);
        }
        return reserva;
    }

    @Override
    @Transactional
    public boolean cancelReservation(Long reservationId, Long userId) {
        Reservation reserva = reservationRepository.findById(reservationId).orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        // Validar que el usuario sea el propietario
        if (!reserva.getUser().getId().equals(userId)) {
            throw new UsuarioNoAutorizadoException("No tienes permiso para cancelar esta reserva");
        }
        // Cambiar estado a cancelada
        StateReservation estadoCancelada = stateReservationRepository.findAll().stream()
            .filter(e -> e.getName().equalsIgnoreCase("CANCELADA")).findFirst().orElseThrow(() -> new EntidadNoEncontradaException("Estado CANCELADA no encontrado"));
        reserva.setState(estadoCancelada);
        reservationRepository.save(reserva);
        return true;
    }

    @Override
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findAll().stream()
            .filter(r -> r.getUser().getId().equals(userId)).toList();
    }
}
