package com.uniquindio.alojamientosAPI.domain.service.reservation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.reservation.ReservationEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.reservation.StateReservationEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.accommodation.AccommodationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.reservation.ReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.reservation.StateReservationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StateReservationRepository stateReservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    private static final String STATE_CONFIRMED = "Confirmada";
    private static final String STATE_CANCELED = "Cancelada";
    private static final String STATE_FINISHED = "Finalizada";

    @Override
    @Transactional
    public ReservationEntity createReservation(Long userId,
                                               Long accommodationId,
                                               LocalDate checkIn,
                                               LocalDate checkOut,
                                               Integer countRoommates) {
        // Validaciones básicas
        if (userId == null || accommodationId == null) {
            throw new IllegalArgumentException("Usuario y alojamiento son obligatorios");
        }
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Fechas de check-in y check-out son obligatorias");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("La fecha de check-in debe ser anterior a la de check-out");
        }
        if (countRoommates == null || countRoommates <= 0) {
            throw new IllegalArgumentException("La cantidad de acompañantes debe ser positiva");
        }

        // Entidades requeridas
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado"));

        // Capacidad
        Integer capacity = accommodation.getCapacity();
        if (capacity != null && capacity > 0 && countRoommates > capacity) {
            throw new IllegalArgumentException("La cantidad de acompañantes excede la capacidad del alojamiento");
        }

        // Disponibilidad (no superposición). Permitimos back-to-back: (A.checkIn < newCheckOut) y (A.checkOut > newCheckIn)
        boolean overlaps = reservationRepository
                .existsByAccommodation_IdAndCheckInLessThanAndCheckOutGreaterThan(accommodationId, checkOut, checkIn);
        if (overlaps) {
            throw new IllegalStateException("El alojamiento no está disponible en el rango solicitado");
        }

        // Estado 'Confirmada'
        StateReservationEntity confirmedState = stateReservationRepository.findByName(STATE_CONFIRMED)
                .orElseThrow(() -> new IllegalStateException("Estado de reserva 'Confirmada' no configurado en BD"));

        // Crear y guardar
        ReservationEntity reservation = ReservationEntity.builder()
                .user(user)
                .accommodation(accommodation)
                .state(confirmedState)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .countRoommates(countRoommates)
                .build();

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        if (reservationId == null || userId == null) {
            throw new IllegalArgumentException("Reserva y usuario son obligatorios");
        }

        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Solo puede cancelar el propietario de la reserva
        Long ownerId = reservation.getUser() != null ? reservation.getUser().getId() : null;
        if (ownerId == null || !ownerId.equals(userId)) {
            throw new IllegalStateException("No autorizado para cancelar esta reserva");
        }

        String currentState = reservation.getState() != null ? reservation.getState().getName() : null;
        if (STATE_CANCELED.equalsIgnoreCase(currentState)) {
            return; // idempotencia: ya cancelada
        }
        if (STATE_FINISHED.equalsIgnoreCase(currentState)) {
            throw new IllegalStateException("No es posible cancelar una reserva finalizada");
        }

        StateReservationEntity canceledState = stateReservationRepository.findByName(STATE_CANCELED)
                .orElseThrow(() -> new IllegalStateException("Estado de reserva 'Cancelada' no configurado en BD"));

        reservation.setState(canceledState);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationEntity> listByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Usuario es obligatorio");
        }
        return reservationRepository.findByUser_Id(userId);
    }
}

