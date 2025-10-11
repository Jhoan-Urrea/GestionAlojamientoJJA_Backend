package com.uniquindio.alojamientosAPI.domain.services.impl;

import com.uniquindio.alojamientosAPI.domain.services.ReservationService;
import com.uniquindio.alojamientosAPI.persistence.entity.Reservation;
import com.uniquindio.alojamientosAPI.persistence.entity.StateReservation;
import com.uniquindio.alojamientosAPI.persistence.entity.ReservationGuest;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.StateReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.ReservationGuesRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.AccommodationRepository;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private StateReservationRepository stateReservationRepository;
    @Autowired
    private ReservationGuesRepository reservationGuesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Override
    @Transactional
    public Reservation createReservation(Long userId, Long accommodationId, LocalDate startDate, LocalDate endDate, java.util.List<Long> guestIds) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        var accommodation = accommodationRepository.findById(accommodationId).orElse(null);
        if (user == null || accommodation == null) return null;
        // Validar fechas
        if (startDate == null || endDate == null || !endDate.isAfter(startDate)) return null;
        // Validar disponibilidad (evitar reservas superpuestas)
        java.util.List<Reservation> reservasExistentes = reservationRepository.findAll();
        for (Reservation r : reservasExistentes) {
            if (r.getAccommodation().getId().equals(accommodationId) &&
                r.getState().getName().equalsIgnoreCase("CONFIRMADA") &&
                !(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate()))) {
                return null;
            }
        }
        StateReservation estadoPendiente = stateReservationRepository.findAll().stream()
            .filter(e -> e.getName().equalsIgnoreCase("PENDIENTE")).findFirst().orElse(null);
        if (estadoPendiente == null) return null;
        Reservation reserva = new Reservation();
        reserva.setUser(user);
        reserva.setAccommodation(accommodation);
        reserva.setStartDate(startDate);
        reserva.setEndDate(endDate);
        reserva.setState(estadoPendiente);
        reserva = reservationRepository.save(reserva);
        // Agregar invitados
        for (Long invitadoId : guestIds) {
            UserEntity invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                ReservationGuest rg = new ReservationGuest();
                rg.setReservation(reserva);
                rg.setGuest(invitado);
                reservationGuesRepository.save(rg);
            }
        }
        return reserva;
    }

    @Override
    @Transactional
    public boolean cancelReservation(Long reservationId, Long userId) {
        Reservation reserva = reservationRepository.findById(reservationId).orElse(null);
        if (reserva == null) return false;
        // Validar que el usuario sea el propietario
        if (!reserva.getUser().getId().equals(userId)) return false;
        // Cambiar estado a cancelada
        StateReservation estadoCancelada = stateReservationRepository.findAll().stream()
            .filter(e -> e.getName().equalsIgnoreCase("CANCELADA")).findFirst().orElse(null);
        if (estadoCancelada == null) return false;
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
